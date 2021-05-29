package com.multi.covid.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
public class ChatbotServiceImpl implements ChatbotService{
	@Autowired
	private ChatbotMapper chatbotMapper;

	
	
	@Override //누적 확진자 조회(전체)
	public String getAllResult() {
		
		ResultVO vo = chatbotMapper.getOneResult("합계");
	
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat wh_format = new SimpleDateFormat ("yyyy년 MM월 dd일");		
		Calendar today1 = Calendar.getInstance();
		today1.add(Calendar.DAY_OF_MONTH, -1);
		Calendar today2 = Calendar.getInstance();
		today2.add(Calendar.DAY_OF_MONTH, -2);

		String result_date = "";
		
		if(vo.getResult_date().equals(format.format(today1.getTime()))) {
			result_date = wh_format.format(today1.getTime());
		}
		else {
			result_date = wh_format.format(today2.getTime());
		}
		vo.getResult_date().equals(result_date);
		
		String[] location_array = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북",
				 "충남", "전북", "전남", "경북", "경남", "제주"};
		StringBuffer webhook_locResult = new StringBuffer();
		for(int i = 0; i < location_array.length; i++) {
			ResultVO one_vo = chatbotMapper.getOneResult(location_array[i]);
			Arrays.sort(location_array);
			webhook_locResult.append(location_array[i] + " 지역 확진자 수: "
					+ one_vo.getIncrement_count() + " \n");
		}
		
		
		JsonObject result_count = new JsonObject();
		result_count.addProperty("getTotal_count", vo.getTotal_count());
		result_count.addProperty("increment_count", vo.getIncrement_count());
		result_count.addProperty("locResult", webhook_locResult.toString().replaceAll(",", ""));
		result_count.addProperty("result_date", result_date);
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("data", result_count);
		
		return result.toString();
	}



	@Override //누적 확진자 조회(지역별)  
	public String getOneResult(String location) {
		//JSON Parsing - result_location 
		//get val location
		
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("result_location").getAsString();		

		//select
		ResultVO vo = chatbotMapper.getOneResult(location);
		
		// simpleText로 출력할 업데이트 시간
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat wh_format = new SimpleDateFormat ("yyyy년 MM월 dd일");		
		Calendar today1 = Calendar.getInstance();
		today1.add(Calendar.DAY_OF_MONTH, -1);
		Calendar today2 = Calendar.getInstance();
		today2.add(Calendar.DAY_OF_MONTH, -2);

		String result_date = "";
		
		if(vo.getResult_date().equals(format.format(today1.getTime()))) {
			result_date = wh_format.format(today1.getTime());
		}
		else {
			result_date = wh_format.format(today2.getTime());
		}
			
		
		//Skill JSON return
		//number format comma (xxx,xxx)
		String getTotal_count = String.format("%,d", vo.getTotal_count());
		String increment_count = String.format("%,d", vo.getIncrement_count());

		String result_message = result_date + " 기준 " + location + "지역의 누적 확진자 수는" + getTotal_count + "입니다.\n\n"
								+ "확진자 수는 " + increment_count + " 명 입니다.";
		String quick_message = location + " 실시간 확진자 조회";
		
		//home button
		JsonObject buttons = new JsonObject();
		buttons.addProperty("label", "처음으로 돌아가기");
		buttons.addProperty("action", "block");
		buttons.addProperty("blockId", "60ade7ebe0891e661e4aad61");
		
		JsonArray buttons_array = new JsonArray();
		buttons_array.add(buttons);

		JsonObject card = new JsonObject();
		card.addProperty("title", result_message);
		card.add("buttons", buttons_array);
		
		JsonObject basicCard = new JsonObject();
		basicCard.add("basicCard", card);
		
		JsonArray outputs_array = new JsonArray();
		outputs_array.add(basicCard);
		
		//quickReplies
		JsonObject quickReplies = new JsonObject();
		quickReplies.addProperty("label", quick_message);
		quickReplies.addProperty("action", "message");
		quickReplies.addProperty("messageText", quick_message);
		
		JsonArray quick_array = new JsonArray();
		quick_array.add(quickReplies);
		
		JsonObject template = new JsonObject();
		template.add("outputs", outputs_array);
		template.add("quickReplies", quick_array);
		
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("template", template);
		
		return result.toString();
		
	}
	
	
	
	@Override //실시간 확진자 조회(전체)
	public String getAllLive() {
		
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		String date = format.format(today.getTime());
		
		//select 
		LiveVO vo = chatbotMapper.getOneLive(date);
		vo.calSum();
		String getSum = String.format("%,d", vo.getSum()); 
		
		String[] loc_name = {"강원도", "경기도", "경상남도", "경상북도", "광주", "대구", "대전", "부산", "서울", 
				"세종", "울산", "인천", "전남", "전북", "제주", "충남", "충북"};
		int[] loc_liveCount = {vo.getGangwon(), vo.getGyeonggi(), vo.getGyeongnam(), vo.getGyeongbuk(), 
				vo.getGwangju(), vo.getDaegu(), vo.getDaejeon(), vo.getBusan(), vo.getSeoul(), vo.getSejong(), 
				vo.getUlsan(), vo.getIncheon(), vo.getJeonnam(), vo.getJeonbuk(), vo.getJeju(), vo.getChungnam(), vo.getChungbuk()};
		StringBuffer webhook_locLive = new StringBuffer();
		for(int i = 0; i < loc_name.length; i++) {
			webhook_locLive.append(loc_name[i] + " 지역 확진자 수: " + loc_liveCount[i] + " 명\n");
		}

		// {{#webhook.total_liveCount}}, {{#webhook.loc_liveCount}} JSON Format
		// 전체, 지역별 전부
		JsonObject total_liveCount = new JsonObject();
		total_liveCount.addProperty("total_liveCount", getSum);
		total_liveCount.addProperty("loc_liveCount", webhook_locLive.toString().replaceAll(",", ""));
		
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("data", total_liveCount);

		return result.toString();
		
	}


	
	@Override //실시간 확진자 조회(지역별) 
	public String getLocLive(String location) {
		
		//JSON Parsing - live_location 
		//get val location
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("live_location").getAsString();
		
		//get val date
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		String date = format.format(today.getTime());
		
		//select
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("location", location);
		map.put("date", date);
		
		int one_count = chatbotMapper.getLocLive(map);
		//db 영어 -> 한글 변환
		String[] eng_loc = {"gangwon", "gyeonggi", "gyeongnam", "gyeongbuk", "gwangju", "daegu", "daejeon", "busan", "seoul", 
				"sejong", "ulsan", "incheon", "jeonnam", "jeonbuk", "jeju", "chungnam", "chungbuk"};
		String[] kor_loc = {"강원도", "경기도", "경상남도", "경상북도", "광주", "대구", "대전", "부산", "서울", 
				"세종", "울산", "인천", "전남", "전북", "제주", "충남", "충북"};
		for(int i = 0; i < eng_loc.length; i++) {
			if(location.equals(eng_loc[i])) {
				location = kor_loc[i];
			}
		}
		
		String live_message = location + " 지역 오늘 확진자 수는" + one_count + " 명 입니다.";
		String quick_message = location + " 누적 확진자 조회";
		
		JsonObject buttons = new JsonObject();
		buttons.addProperty("label", "처음으로 돌아가기");
		buttons.addProperty("action", "block");
		buttons.addProperty("blockId", "60ade7ebe0891e661e4aad61");
		
		JsonArray buttons_array = new JsonArray();
		buttons_array.add(buttons);

		JsonObject card = new JsonObject();
		card.addProperty("title", live_message);
		card.add("buttons", buttons_array);
		
		JsonObject basicCard = new JsonObject();
		basicCard.add("basicCard", card);
		
		JsonArray outputs_array = new JsonArray();
		outputs_array.add(basicCard);
		
		JsonObject quickReplies = new JsonObject();
		quickReplies.addProperty("label", quick_message);
		quickReplies.addProperty("action", "message");
		quickReplies.addProperty("messageText", quick_message);
		
		JsonArray quick_array = new JsonArray();
		quick_array.add(quickReplies);
		
		JsonObject template = new JsonObject();
		template.add("outputs", outputs_array);
		template.add("quickReplies", quick_array);
		
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("template", template);
		
		return result.toString();
		
		
	}

	
	@Override//백신센터 지역별(도, 시, 읍/면/리) / (block 추가해야 함)  
	public String getLocCenter(String location) {
		
		//get val location
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("vaccine_address_two").getAsString();
		
		//'백신센터'제거
		location = location.replace("백신센터", "");
		
		//select
		List<CenterVO> vo = chatbotMapper.getLocCenter(location);
		
		StringBuffer facility_name = new StringBuffer();		
		for(int i = 0; i < vo.size(); i++) {
			facility_name.append(vo.get(i).getFacility_name() + "\n");
		}
		
		//{{#webhook.r_facility_name}} JSON Format
		JsonObject reg_center = new JsonObject();
		reg_center.addProperty("r_facility_name", facility_name.toString().replaceAll(",", ""));
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("data", reg_center);
		
		return reg_center.toString();
		
	}



	@Override//지역 선택 바로가기버튼 생성 
	public String selectAddrTwo(String location) {
		
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("vaccine_center_location").getAsString();
		
		List<String> addr = chatbotMapper.getAddrTwo(location);
		ArrayList<String> addr_arr = new ArrayList<String>();
		
		//바로가기버튼 20개로 제한
		if(addr.size() >= 20) {
			for(int i = 0; i < 19; i++) {
				String[] addr_split = addr.get(i).split(" ");
				addr_arr.add(addr_split[1]);
			}
			addr_arr.add("그 외 " + "(" + location + ")");
		}
		else {
			for(int i = 0; i < addr.size(); i++) {	
				addr_arr.add(addr.get(i));
			}
		}
		
		JsonArray quick_item_arr = new JsonArray();
		
		for(int i = 0; i < addr_arr.size(); i++) {
		JsonObject quickReplies = new JsonObject();
			quickReplies.addProperty("label", addr_arr.get(i));
			quickReplies.addProperty("action", "message");
			quickReplies.addProperty("messageText", addr.get(i));
			quick_item_arr.add(quickReplies);
		}
		
		
		JsonArray quick_array = new JsonArray();
		for(int i = 0; i < quick_item_arr.size(); i++) {
			quick_array.add(quick_item_arr.get(i));
		}
		
		JsonObject text = new JsonObject();
		text.addProperty("text", "원하시는 지역을 선택하세요.");
		
		JsonObject simpleText = new JsonObject();
		simpleText.add("simpleText", text);
		
		JsonArray outputs_array = new JsonArray();
		outputs_array.add(simpleText);
		
		JsonObject template = new JsonObject();
		template.add("outputs", outputs_array);
		template.add("quickReplies", quick_array);
		
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("template", template);
		
		return result.toString();
		
		
	}
	
	
	
	
	@Override//읍, 면, 리 바로가기버튼 생성 (최대 20)
	public String selectAddrThree(String location) {
		
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("vaccine_address_two").getAsString();
		
		List<String> addr = chatbotMapper.getAddrThree(location);
		List<CenterVO> center_count = chatbotMapper.getAddrCenter(location);
		
		ArrayList<String> addr_arr = new ArrayList<String>();
		//String another = "";
		String result_string = null;
		
		//세번째 주소 index가 없는 경우 
		boolean third_check = false;
		
		if(addr.size() == 1) {
			third_check = true;
		}
		else {
		
			if(addr.size() >= 20) {
				for(int i = 0; i < 19; i++) {
					String[] addr_split = addr.get(i).split(" ");
					addr_arr.add(addr_split[2]);
					//another = addr_split[1];
				}
				addr_arr.add(location + " 더 찾기");
			}
			else {
				for(int i = 0; i < addr.size(); i++) {	
					String[] addr_split = addr.get(i).split(" ");		
					addr_arr.add(addr_split[2]);
				}
			}
			
		}
		
		//예외 1) 두번째 주소까지 조회시 리스트가 5개 이하 -> getAddCenter메소드 실행(링크 출력)
		//예외 2) 리스트 5개 이상, 세번째 주소 index가 없는 경우 리스트출력 (ex. 세종특별자치시 한누리대로)
		if(center_count.size()<=5 || third_check) {
			result_string = getAddrCenter(location);
		}
		else {
			//바로가기버튼 20개로 제한
			
			JsonArray quick_item_arr = new JsonArray();
			
			for(int i = 0; i < addr_arr.size(); i++) {
			JsonObject quickReplies = new JsonObject();
				quickReplies.addProperty("label", addr_arr.get(i));
				quickReplies.addProperty("action", "message");
				quickReplies.addProperty("messageText", addr.get(i));
				quick_item_arr.add(quickReplies);
			}
			
			
			JsonArray quick_array = new JsonArray();
			for(int i = 0; i < quick_item_arr.size(); i++) {
				quick_array.add(quick_item_arr.get(i));
			}
			
			JsonObject text = new JsonObject();
			text.addProperty("text", location + " 을 선택하셨습니다. \n원하시는 지역을 선택하세요.");
			
			JsonObject simpleText = new JsonObject();
			simpleText.add("simpleText", text);
			
			JsonArray outputs_array = new JsonArray();
			outputs_array.add(simpleText);
			
			JsonObject template = new JsonObject();
			template.add("outputs", outputs_array);
			template.add("quickReplies", quick_array);
			
			JsonObject result = new JsonObject();
			result.addProperty("version", "2.0");
			result.add("template", template);
			
			result_string = result.toString();
		}
		
		return result_string;
		
	}
	
	
	@Override//20개 초과 버튼 처리
	public String selectAddrRemainder(String location) {
		
		boolean paramCheck = false;
		String paramName = "";
		if(location.contains("vaccine_center_location")) {
			paramName = "vaccine_center_location";
			paramCheck = true;
		}
		else {
			paramName = "vaccine_address_two";
			
		}
		
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get(paramName).getAsString();


		List<String> addr_two = chatbotMapper.getAddrTwo(location);
		List<String> addr_three = chatbotMapper.getAddrThree(location);
		
		ArrayList<String> addr_arr = new ArrayList<String>();
		
		if(paramCheck) {
			
			if(addr_two.size() >= 40) {
				for(int i = 20; i < 39; i++) {
					String[] addr_split = addr_two.get(i).split(" ");
					addr_arr.add(addr_split[1]); 
				}
				addr_arr.add("주소 직접 입력하기");	
			}	
			else {
				for(int i = 20; i < addr_two.size(); i++) {
					String[] addr_split = addr_two.get(i).split(" ");
					addr_arr.add(addr_split[1]); 
				}	
			}
		}
		else {
			
			if(addr_three.size() >= 40) {
				for(int i = 20; i < 39; i++) {
					String[] addr_split = addr_three.get(i).split(" ");
					addr_arr.add(addr_split[1]); 
				}
				addr_arr.add("주소 직접 입력하기");	
			}	
			else {
				for(int i = 20; i < addr_three.size(); i++) {
					String[] addr_split = addr_three.get(i).split(" ");
					addr_arr.add(addr_split[1]); 
				}	
			}
			
		}
		
		JsonArray quick_items_arr = new JsonArray();
		

		for(int i = 0; i < addr_arr.size(); i++) {
			
			JsonObject quickReplies = new JsonObject();
			
			quickReplies.addProperty("label", addr_arr.get(i));
			quickReplies.addProperty("action", "message");
			quickReplies.addProperty("messageText", location + " " + addr_arr.get(i));
			quick_items_arr.add(quickReplies);
		}
		
		JsonArray quick_array = new JsonArray();
		for(int i = 0; i < quick_items_arr.size(); i++) {
			quick_array.add(quick_items_arr.get(i));
		}
		
		JsonObject text = new JsonObject();
		text.addProperty("text", "원하시는 지역을 선택하세요.");
		
		JsonObject simpleText = new JsonObject();
		simpleText.add("simpleText", text);
		
		JsonArray outputs_array = new JsonArray();
		outputs_array.add(simpleText);
		
		JsonObject template = new JsonObject();
		template.add("outputs", outputs_array);
		template.add("quickReplies", quick_array);
		
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("template", template);
		
		return result.toString();

	}
	
	
	
	@Override//백신센터 지역별(시, 군, 구) + 주소링크
	public String getAddrCenter(String location) {	
		
		//JSON으로 받아올 경우
		if(location.contains(":")) {
			JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
			JsonElement action = jsonObj.get("action");
			JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
			location = params.getAsJsonObject().get("vaccine_address_three").getAsString();
		} 
		
		//select
		List<CenterVO> vo = chatbotMapper.getAddrCenter(location);
		
		
		
		int endNum = 0;
		int startNum = 0;

		boolean overLength = false;
		boolean overLength15 = false;
		if(vo.size() > 5) {
			startNum = 0;
			endNum = 5;
			overLength = true;
		}
		else if(vo.size() > 10) {
			startNum = 5;
			endNum = 10;
			overLength = true;
		}
		else if(vo.size() > 15) {
			startNum = 10;
			endNum = 15;
			overLength15 = true;
		}
		else {
			startNum = vo.size();
		}
		
		JsonArray setItems_array = new JsonArray(); 
		for(int i = startNum; i < endNum; i++) {
			//주소 링크 생성
			String address_url = "https://map.kakao.com/link/map/" 
			+ vo.get(i).getFacility_name().replaceAll(" ", "") + "," + vo.get(i).getLat() + "," + vo.get(i).getLng();
			JsonObject web = new JsonObject();
			web.addProperty("web", address_url);
			
			JsonObject items = new JsonObject();
			items.addProperty("title", vo.get(i).getFacility_name());
			items.addProperty("description", vo.get(i).getAddress());
			items.add("link", web);
			
			setItems_array.add(items);
		}
		
		//리스트가 5-10개 초과일 경우, '더 보기' 버튼 생성/ 리스트가 15개 초과일 경우, '지역별 센터 리스트 보기'
		
		String label = "";
		String messageText = "";
		if(overLength) {
			label = "더 보기";
			messageText = location;
		}
		if(overLength15) {
			label = "지역별 센터 리스트 보기";
			messageText = "지역별 센터 리스트 보기";
		}
		
		JsonArray quick_array = new JsonArray();
		if(overLength || overLength15) {
			JsonObject quickReplies = new JsonObject();
			
			quickReplies.addProperty("label", label);
			quickReplies.addProperty("action", messageText);
			quickReplies.addProperty("messageText", location);

			quick_array.add(quickReplies);
		}
		
		JsonObject title = new JsonObject();
		title.addProperty("title", location + "지역의 백신 센터");
		
		JsonArray items_array = new JsonArray();
		for(int i = 0; i < setItems_array.size(); i++) {
			items_array.add(setItems_array.get(i));
		}
		
		//home button
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
		if(overLength || overLength15) {
			template.add("quickReplies", quick_array);
		}
		
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("template", template);

		return result.toString();
	 	
		}


	@Override//리스트 5개 초과
	public String getAddrCenter_2(String location) {
		
		return getAddrCenter(location);
	}



	@Override//리스트 15개 초과
	public String getAddrCenter_3(String location) {
		return getAddrCenter(location);
	}


}