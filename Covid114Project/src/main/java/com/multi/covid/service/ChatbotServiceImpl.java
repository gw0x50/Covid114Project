package com.multi.covid.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.ChatbotMapper;

@Service
public class ChatbotServiceImpl implements ChatbotService {
	@Autowired
	private ChatbotMapper chatbotMapper;

	// BasicCard형 JSON
	@Override
	public String getCardJsonString(JsonArray quick_array, String title_message) {

		JsonObject buttons = new JsonObject();
		buttons.addProperty("label", "처음으로 돌아가기");
		buttons.addProperty("action", "block");
		buttons.addProperty("blockId", "60ade7ebe0891e661e4aad61");

		JsonArray buttons_array = new JsonArray();
		buttons_array.add(buttons);

		JsonObject card = new JsonObject();
		card.addProperty("title", title_message);
		card.add("buttons", buttons_array);

		JsonObject basicCard = new JsonObject();
		basicCard.add("basicCard", card);

		JsonArray outputs_array = new JsonArray();
		outputs_array.add(basicCard);

		JsonObject template = new JsonObject();
		template.add("outputs", outputs_array);
		template.add("quickReplies", quick_array);

		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("template", template);

		return result.toString();
	}

	// 조회값 null 발생 시 Text형 JSON return 	
	@Override
	public String getTextJsonString() {
		
		String title_message = "\n검색하신 진료소(장소)에는 백신 접종 센터가 존재하지 않습니다.\n\n";
		// 두 개의 quickReplies 출력
		
		String quick_message = "백신 접종 센터 조회 처음으로";
		String actionName = "block";
		String action_item = "60a929eb9657424ac11d8d29";

		// quickReplies
		JsonArray quick_array = new JsonArray();

		JsonObject quickReplies = new JsonObject();
		quickReplies.addProperty("label", quick_message);
		quickReplies.addProperty("action", actionName);
		quickReplies.addProperty("blockId", action_item);

		quick_array.add(quickReplies);
		
		// JSON basicCard
		return getCardJsonString(quick_array, title_message);
	}

	// 누적 확진자 조회
	@Override
	public String getResult(String location) {

		String resultJson = null;
		boolean paramLoc_check = false;

		if (location.contains("result_location")) { // 지역으로 조회 
			JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
			JsonElement action = jsonObj.get("action");
			JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
			location = params.getAsJsonObject().get("result_location").getAsString();
			paramLoc_check = true;
		}
		else {
			location = "합계";
		}

		// select
		ResultVO vo = chatbotMapper.getResult(location);

		// update date
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat output_format = new SimpleDateFormat("yyyy년 MM월 dd일");
		Calendar today1 = Calendar.getInstance();
		today1.add(Calendar.DAY_OF_MONTH, -1);
		Calendar today2 = Calendar.getInstance();
		today2.add(Calendar.DAY_OF_MONTH, -2);

		String result_date = "";
		// 업데이트 날짜 확인 (당일 업데이트 전 -2, 업데이트 후 -1)
		if (vo.getResult_date().equals(format.format(today1.getTime()))) {
			result_date = output_format.format(today1.getTime());
		}
		else {
			result_date = output_format.format(today2.getTime());
		}

		// number format comma (xxx,xxx)
		String getTotal_count = String.format("%,d", vo.getTotal_count());
		String increment_count = String.format("%,d", vo.getIncrement_count());

		if (paramLoc_check) { // Result one(지역) 조회일 경우
			String title_message = location + "지역 누적 확진자 수: " + getTotal_count + "\n\n" + result_date + " 확진자 수: " + increment_count + " 명";
			String quick_message = location + " 실시간 확진자 조회";

			// quickReplies
			JsonObject quickReplies = new JsonObject();
			quickReplies.addProperty("label", quick_message);
			quickReplies.addProperty("action", "message");
			quickReplies.addProperty("messageText", quick_message);

			JsonArray quick_array = new JsonArray();
			quick_array.add(quickReplies);
			// JSON(basicCard)
			resultJson = getCardJsonString(quick_array, title_message);
		}
		else { // Result all(전체)
			String[] location_array = { "서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주" };
			StringBuffer webhook_locResult = new StringBuffer();

			for (int i = 0; i < location_array.length; i++) { //지역 전체 값 출력
				ResultVO one_vo = chatbotMapper.getResult(location_array[i]);
				Arrays.sort(location_array);
				String getTotal = String.format("%,d", one_vo.getTotal_count());
				webhook_locResult.append(location_array[i] + " 지역: " + getTotal + " \n");
			}

			// JSON(webhook) 
			JsonObject result_count = new JsonObject();
			result_count.addProperty("locResult", webhook_locResult.toString().replaceAll("\n,", ""));
			result_count.addProperty("getTotal_count", getTotal_count);
			result_count.addProperty("increment_count", increment_count);
			result_count.addProperty("result_date", result_date);
			JsonObject result = new JsonObject();
			result.addProperty("version", "2.0");
			result.add("data", result_count);

			resultJson = result.toString();
		}

		return resultJson;
	}

	// 실시간 확진자 조회(전체/지역) 
	@Override
	public String getLive(String location) {

		String resultJson = null;
		boolean paramLoc_check = false; // 지역으로 조회 체크

		if (location.contains("live_location")) { // 지역으로 조회할 경우 
			JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
			JsonElement action = jsonObj.get("action");
			JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
			location = params.getAsJsonObject().get("live_location").getAsString();

			paramLoc_check = true;
		}

		// get val date
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		String date = format.format(today.getTime());

		String[] eng_loc = { "gangwon", "gyeonggi", "gyeongnam", "gyeongbuk", "gwangju", "daegu", "daejeon", "busan", "seoul", "sejong", "ulsan",
				"incheon", "jeonnam", "jeonbuk", "jeju", "chungnam", "chungbuk" };
		String[] kor_loc = { "강원", "경기", "경남", "경북", "광주", "대구", "대전", "부산", "서울", "세종", "울산", "인천", "전남", "전북", "제주", "충남", "충북" };

		if (paramLoc_check) { // Live one(지역)
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("location", location);
			map.put("date", date);
			int one_count = chatbotMapper.getLocLive(map);

			// 지역명 한글로 출력(DB 영어 -> 한글)
			for (int i = 0; i < eng_loc.length; i++) {
				if (location.equals(eng_loc[i])) {
					location = kor_loc[i];
				}
			}

			String title_message = location + " 지역 오늘 확진자 수는 " + one_count + " 명 입니다.";
			String quick_message = location + " 누적 확진자 조회";

			// quickReplies 
			JsonObject quickReplies = new JsonObject();
			quickReplies.addProperty("label", quick_message);
			quickReplies.addProperty("action", "message");
			quickReplies.addProperty("messageText", quick_message);

			JsonArray quick_array = new JsonArray();
			quick_array.add(quickReplies);

			// JSON(BasicCard)
			resultJson = getCardJsonString(quick_array, title_message);
		}
		else { // Live all(전체)
				// 전국(지역별 합산) 
			LiveVO vo = chatbotMapper.getLive(date);
			vo.calSum();
			String getSum = String.format("%,d", vo.getSum());

			// 지역별 확진자 수
			int[] loc_liveCount = { vo.getGangwon(), vo.getGyeonggi(), vo.getGyeongnam(), vo.getGyeongbuk(), vo.getGwangju(), vo.getDaegu(),
					vo.getDaejeon(), vo.getBusan(), vo.getSeoul(), vo.getSejong(), vo.getUlsan(), vo.getIncheon(), vo.getJeonnam(), vo.getJeonbuk(),
					vo.getJeju(), vo.getChungnam(), vo.getChungbuk() };

			StringBuffer webhook_locLive = new StringBuffer();
			for (int i = 0; i < kor_loc.length; i++) {
				webhook_locLive.append(kor_loc[i] + " 지역 확진자 수: " + loc_liveCount[i] + " 명\n");
			}

			// JSON(webhook)
			JsonObject total_liveCount = new JsonObject();
			total_liveCount.addProperty("total_liveCount", getSum);
			total_liveCount.addProperty("loc_liveCount", webhook_locLive.toString().replaceAll(",", ""));

			JsonObject result = new JsonObject();
			result.addProperty("version", "2.0");
			result.add("data", total_liveCount);

			resultJson = result.toString();
		}

		return resultJson;
	}

	// 백신센터 지역별(도, 시)  
	@Override
	public String getCenterList(String location) {
		
		// get val location 
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("vaccine_address").getAsString();
		
		if (location.contains("전체")) {
			location = location.replace(" 전체", "");
		}

		// select
		List<CenterVO> vo = chatbotMapper.getAddrCenter(location);

		// 해당 지역 리스트 모두 출력
		StringBuffer facility_name = new StringBuffer();
		for (int i = 0; i < vo.size(); i++) {
			facility_name.append(vo.get(i).getFacility_name() + "\n");
		}

		// JSON(webhook)
		JsonObject reg_center = new JsonObject();
		reg_center.addProperty("location", location);
		reg_center.addProperty("facility_name", facility_name.toString().replaceAll(",", ""));
		reg_center.addProperty("facility_count", vo.size());
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("data", reg_center);

		return reg_center.toString();
	}

	// 지역 바로가기 버튼
	@Override
	public String selectAddr(String location) {

		String param = "";
		int endNum = 20; // 반복문 제한 
		int startNum = 0; // 반복문 시작 
		// 요청 Parameter 체크 
		boolean paramCheck_one = false;
		boolean paramCheck_two = false;
		boolean remainderCheck = false;

		if (location.contains(":")) { // POST로 받는 경우			
			if (location.contains("over")) {// 20개 초과 버튼 확인(" 더 찾기")
				remainderCheck = true;
				endNum = 40;
				startNum = 20;
			}
			// context로 연결되어 있기 때문에 블록의 고유값으로 파라미터 구분
			if (location.contains("60adf9082c7d75439efb91ca")) { // 주소값 한 개
				param = "vaccine_address_one";
				paramCheck_one = true;
			}
			else if (location.contains("60b077e098179667c00efdf7")) { // 주소값 두 개 
				param = "vaccine_address_two";
				paramCheck_two = true;
			}
			else if (location.contains("60b077b398179667c00efdee")) { // 20개 초과 
				if(location.contains("vaccine_address_one")) { // 주소값 한 개 
					param = "vaccine_address_one";
					paramCheck_one = true;
				}
				else if (location.contains("vaccine_find_more_two")) { // 주소값 두 개 
					param = "vaccine_find_more_two";
					paramCheck_two = true;					
				}
				else { // 주소값 세 개 
					param = "vaccine_find_more_three";
				}
			}
			JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
			JsonElement action = jsonObj.get("action");
			JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
			location = params.getAsJsonObject().get(param).getAsString();
			}
		
		List<String> addr = null; // 출력할 주소 

		int center_count = 20; // 예외 처리할 리스트 개수 확인(기본값 20)
		int splitNum = 0; // 출력할 주소 index값  

		if (paramCheck_one) {
			addr = chatbotMapper.getAddrTwo(location);
			splitNum = 1;
		}
		else if (paramCheck_two) {
			addr = chatbotMapper.getAddrThree(location);
			if (!remainderCheck) {
				center_count = chatbotMapper.getAddrCenter(location).size();
			}
			splitNum = 2;
		}
		else {
			addr = chatbotMapper.getAddrFour(location);
			splitNum = 3;
		}

		ArrayList<String> addr_arr = new ArrayList<String>();
		if (addr.size() > endNum) { // 출력 20 초과			
			for (int i = startNum; i < endNum - 1; i++) {
				String[] addr_split = addr.get(i).split(" ");
				try {
					addr_arr.add(addr_split[splitNum]);
				}
				catch (ArrayIndexOutOfBoundsException e) { // 주소 index가 존재하지 않는 경우(=index가 숫자인 경우) 
					addr_arr.add(location + " 전체");
				}
			}
			if (remainderCheck) { // 40개 초과
				addr_arr.add(" 주소 직접 입력하기");
			}
			else { // 20개 초과
				addr_arr.add(location + " 더 찾기");
			}
		}
		else { // 출력 20 이하 
			for (int i = startNum; i < addr.size(); i++) {
				String[] addr_split = addr.get(i).split(" ");
				try {
					addr_arr.add(addr_split[splitNum]);
				}
				catch (ArrayIndexOutOfBoundsException e) {
					addr_arr.add(location + " 전체");
				}
			}
		}

		String result_string = null;

		/*
		 *아래 예외에서는 버튼 생성X, 바로 리스트 출력
		 *예외 1) 두번째 주소까지 조회시 리스트 5개 이하
		 *예외 2) 리스트 5개 이상, 세번째 주소 index가 없는 경우(= 숫자인 경우)
		 */
		if (center_count <= 5 || (center_count < 15 && addr_arr.size() == 0)) {
			result_string = getCenterUrl(location, center_count);
		}
		else {
			// 선택 문구 메시지 
			String title_message = location + "을 선택하셨습니다. \n세부 주소를 선택해 주세요.";
			// quickReplies(지역 선택 버튼)
			JsonArray quick_item_arr = new JsonArray();
			for (int i = 0; i < addr_arr.size(); i++) {

				JsonObject quickReplies = new JsonObject();
				quickReplies.addProperty("label", addr_arr.get(i));
				quickReplies.addProperty("action", "message");
				quickReplies.addProperty("messageText", location + " " + addr_arr.get(i)); // addr.get(i) 사용시 20개 초과버튼 값 0부터 초기화 

				if (!remainderCheck && addr_arr.get(i).contains(location)) { // 주소값 두 개 선택지 이동 
					quickReplies.addProperty("blockId", "60b077b398179667c00efdee");
					quickReplies.addProperty("messageText", addr_arr.get(i)); 
				}
				quick_item_arr.add(quickReplies);
			}

			JsonArray quick_array = new JsonArray();
			for (int i = 0; i < quick_item_arr.size(); i++) {
				quick_array.add(quick_item_arr.get(i));
			}
			// JSON basicCard
			result_string = getCardJsonString(quick_array, title_message);
		}

		return result_string;
	}

	// 센터 주소 링크 리스트
	@Override
	public String getCenterUrl(String address, int lengthNum) {

		// select
		List<CenterVO> vo = null;
		boolean facility_check = false;
		boolean addressFourNull_check = false;
		boolean facility_over5_check = false;

		if (address.contains(":")) { // POST로 받는 경우
			JsonObject jsonObj = (JsonObject) JsonParser.parseString(address);
			JsonElement action = jsonObj.get("action");
			JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
			try {				
				address = params.getAsJsonObject().get("vaccine_address").getAsString();
			} 
			catch (NullPointerException e) { // 주소값 입력이 올바르지 않을 경우 
				// JSON(text)
				return getTextJsonString();
			}
			if (address.contains(" 전체")) { // 4개 주소값 예외처리(네번째 값 null인 경우) 
				address = address.replaceAll(" 전체", "");
				addressFourNull_check = true;
			}
		}

		
		if (address.contains(" ")) { // 주소값으로 넘어온 경우
			vo = chatbotMapper.getAddrCenter(address);
		}
		else if (address.contains(",")) { // facilityCheck에서 지역 예외처리 후 넘어온 경우 
			String[] facilityAndLoc = address.split(",");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("location", facilityAndLoc[0]);
			map.put("facility_name", facilityAndLoc[1]);

			vo = chatbotMapper.getFacility_loc(map);
			facility_over5_check = true;
		}
		else { // facilityCheck에서 넘어온 경우
			vo = chatbotMapper.getFacility(address);
			facility_check = true;
		}

		/* 
		 * 링크 출력 최대 5개, 3번까지(총 15개) 반복
		 * 5개 미만, 5개 이상, 10개 이상, 15개 이상 처리
		 */
		int endNum = 0; // 반복문 제한 
		int startNum = 0; // 반복문 시작 

		boolean overLength = false;
		boolean overLength15 = false;
		boolean length10 = false;

		if (lengthNum > 5) {
			if (lengthNum == 10) { // 10개인 경우  
				length10 = true;
			}
			else if (lengthNum == 100) { // 10개 초과 15개 미만
				lengthNum = 10;
			}

			if (lengthNum > 5 && lengthNum <= 10) {
				startNum = 5;
				endNum = lengthNum;
				overLength = true;
			}
			else if (lengthNum <= 15) {
				startNum = 10;
				endNum = lengthNum;
				overLength = true;
			}
			else {
				startNum = 10;
				endNum = 15;
				overLength15 = true;
			}
		}
		else if (vo.size() > 5) { // POST로 받는 경우 lengthNum 할당

			// 네번째 주소 버튼 호출 후, 값이 없는 경우 버튼 출력 
			if (vo.size() > 20 && chatbotMapper.getAddrFour(address).size() != 0 && !addressFourNull_check) {
				return selectAddr(address);
			}
			startNum = 0;
			endNum = 5;
			overLength = true;
			lengthNum = 5;
		}
		else {
			startNum = 0;
			endNum = vo.size();
			lengthNum = vo.size();
		}

		// 주소 링크 생성 
		JsonArray setUrl_array = new JsonArray();
		for (int i = startNum; i < endNum; i++) {
			
			// Kakao map id
			// 의료기관 DB 정보와 카카오 맵 업데이트 차이 있을 경우 시설명으로 조회  
			String id = getKakaoMapId(vo.get(i).getCenter_name() + "," + vo.get(i).getFacility_name());

			String address_url = "https://map.kakao.com/link/map/" + id;
			JsonObject web = new JsonObject();
			web.addProperty("web", address_url); // url 

			JsonObject items = new JsonObject();
			items.addProperty("title", vo.get(i).getFacility_name()); // 시설명
			items.addProperty("description", vo.get(i).getAddress()); // 시설 주소
			items.add("link", web);

			setUrl_array.add(items);
		}

		/* 
		 * 리스트가 5/10개 초과일 경우, '더 보기' 버튼 
		 * 리스트가 15개 초과일 경우, '지역 센터 리스트 보기' 버튼
		 */
		String label = "";
		String action_item = "";

		if (endNum % 5 == 0 && overLength && !length10) {
			label = "더 보기";
			if (lengthNum == 5) { // 5개 초과 
				if (facility_over5_check) { // facilityCheck값이 5개 초과인 경우의 블록
					action_item = "60bacfb0cb6ae85c16a00f93";
				}
				else {
					action_item = "60b22434f6266b70b5289df6";
				}
			}
			else { // 10개 초과 
				action_item = "60b2406f2c7d75439efba8a4";
			}
		}
		else if (overLength15) { // 15개 초과
			label = address + " 지역 센터 리스트 전체 보기";
			action_item = "60abb421b93ffe67f982acf2";
		}
		else { // 검색형
			label = "다시 검색하기";
			action_item = "60b09b759cf5b44e9f808a62";
		}

		// JSON listCard
		// quickReplies
		JsonArray quick_array = new JsonArray();
		if (overLength15 || endNum % 5 == 0 && overLength || facility_check) {
			JsonObject quickReplies = new JsonObject();

			quickReplies.addProperty("label", label);
			quickReplies.addProperty("action", "block");
			quickReplies.addProperty("blockId", action_item);
			quick_array.add(quickReplies);
		}

		String cardTitle = address + " 백신 센터"; // 리스트 큰 제목 

		JsonArray items_array = new JsonArray();
		for (int i = 0; i < setUrl_array.size(); i++) {
			items_array.add(setUrl_array.get(i));
		}

		JsonObject title = new JsonObject();
		title.addProperty("title", cardTitle);

		JsonObject buttons = new JsonObject();
		buttons.addProperty("label", "처음으로 돌아가기");
		buttons.addProperty("action", "block");
		buttons.addProperty("blockId", "60ade7ebe0891e661e4aad61");

		JsonArray buttons_array = new JsonArray();
		buttons_array.add(buttons);

		JsonObject header = new JsonObject();
		header.add("items", items_array);
		header.add("header", title);
		header.add("buttons", buttons_array);

		JsonObject listCard = new JsonObject();
		listCard.add("listCard", header);

		JsonArray outputs_array = new JsonArray();
		outputs_array.add(listCard);

		JsonObject template = new JsonObject();
		template.add("outputs", outputs_array);
		template.add("quickReplies", quick_array);

		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("template", template);

		return result.toString();
	}

	// 센터 주소 링크 리스트 (리스트 5개 초과)
	@Override
	public String getCenterUrl_over(String address) {

		boolean overTen_check = false;
		if (address.contains("over10")) {
			overTen_check = true;
		}
		// get address
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(address);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		address = params.getAsJsonObject().get("vaccine_address").getAsString();

		if (address.contains(" 전체")) { // 발화 파라미터값 처리 
			address = address.replace(" 전체", "");
		}
		// select
		List<CenterVO> vo = chatbotMapper.getAddrCenter(address);

		int lengthNum = 0;
		if (overTen_check) { // 15개 초과
			lengthNum = vo.size();
		}
		else if (vo.size() <= 10) { // 10 이하
			lengthNum = vo.size();
		}
		else { // 10 초과
			lengthNum = 100; // 10일 경우와 분리
		}

		// JSON listCard
		return getCenterUrl(address, lengthNum);
	}

	// 백신 센터 직접 검색, 센터 주소 링크 리스트
	@Override
	public String facilityCheck(String facility_name) {

		String resultJson = "";
		List<CenterVO> vo = null;

		boolean locationCheck = false; // 5개 초과, 지역값과 함께 들어온 파라미터 확인
		boolean locationOver = false; // 시설명 + 지역명 5개 초과 파라미터 확인 
		String location = "";
		if (facility_name.contains("vaccine_address_one")) {
			locationCheck = true;
			if (facility_name.contains("over")) {
				locationOver = true;
			}
		}
		
		// get location, facility_name
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(facility_name);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();

		if (locationCheck) {
			location = params.getAsJsonObject().get("vaccine_address_one").getAsString();
		}
		
		facility_name = params.getAsJsonObject().get("facility_name").getAsString();
		facility_name = facility_name.replace(" ", ""); // 글자 사이 공백을 넣어 검색했을 경우	
	 	vo = chatbotMapper.getFacility(facility_name);

		if (vo.size() == 0) { // 입력한 진료소가 존재하지 않는 경우
			// JSON(text)
			return getTextJsonString();
		}

		TreeSet<String> getLoc_set = new TreeSet<String>();

		if (vo.size() > 5 && !locationCheck) { // 리스트 개수 5개 초과 
			for (CenterVO loc : vo) { // facility_name의 location 조회 
				String[] getLoc = loc.getAddress().split(" ");
				getLoc_set.add(getLoc[0]);
			}

			String title_message = "\n" + facility_name + " 조회 결과, 전국에 " + vo.size() + " 개가 있습니다.\n지역을 선택해 주세요.\n";

			// quickReplies 
			JsonArray quick_array = new JsonArray();
			for (String loc : getLoc_set) {
				JsonObject quickReplies = new JsonObject();
				quickReplies.addProperty("label", loc);
				quickReplies.addProperty("action", "message");
				quickReplies.addProperty("messageText", loc + " 지역으로 접종 센터 조회");

				quick_array.add(quickReplies);
			}

			resultJson = getCardJsonString(quick_array, title_message);
		}
		else {
			if (locationCheck) { // 5개 초과 후 시설명 + 선택한 지역명 받아온 경우
				if (locationOver) { // 시설명 + 지역명 리스트 5개 초과 
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("location", location);
					map.put("facility_name", facility_name);
					int center_length = chatbotMapper.getFacility_loc(map).size();
					// JSON listCard
					resultJson = getCenterUrl(location + "," + facility_name, center_length);
				}
				else { // 시설명 + 지역명 리스트 5개 이하 
						// JSON listCard
					resultJson = getCenterUrl(location + "," + facility_name, 1);
				}
			}
			else { // 시설명 리스트 5개 이하 
					// JSON listCard 
				resultJson = getCenterUrl(facility_name, vo.size());
			}
		}

		return resultJson;
	}
	
	// 카카오맵ID
	@Override
	public String getKakaoMapId(String facility_name) {

		String apiKey = "649061c6abd5ffc886277e7f9a91a020";
		String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json";
		String jsonString = null;
		String [] facility_name_arr = null;
		try {
			facility_name_arr = facility_name.split(",");
			facility_name = facility_name_arr[0]; // 의료기관 이름
			facility_name = URLEncoder.encode(facility_name, "UTF-8");

			String addr = apiUrl + "?query=" + facility_name;

			URL url = new URL(addr);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);

			BufferedReader rd = null;
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuffer docJson = new StringBuffer();

			String line;

			while ((line = rd.readLine()) != null) {
				docJson.append(line);
			}

			jsonString = docJson.toString();
			rd.close();

		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		// KakaoMap ID return
		String id = "";
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(jsonString);
		JsonArray documents = jsonObj.get("documents").getAsJsonArray();
		try {
			id = documents.get(0).getAsJsonObject().get("id").getAsString();
		}
		catch (IndexOutOfBoundsException e) { // 의료센터가 카카오 맵에 업데이트되지 않았을 경우, 시설명으로 조회
			id = getKakaoMapId(facility_name_arr[1] + "," + facility_name_arr[0]);
		}
		
		return id;
	}

}