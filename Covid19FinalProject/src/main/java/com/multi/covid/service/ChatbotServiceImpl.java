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
		//1) simpleText - 확진자 정보	
		//2) quickReplies - (message: ㅇㅇ지역 실시간 확진 조회), covid_liveLoc block 실행  
		//3) button - 시작블록으로 돌아가는 버튼 
		// quickReplies, outputs는 array type 
		
		//number format comma (xxx,xxx)
		String getTotal_count = String.format("%,d", vo.getTotal_count());
		String increment_count = String.format("%,d", vo.getIncrement_count());

		String result_message = result_date + " 기준 " + location + "지역의 누적 확진자 수는" + getTotal_count + "입니다.\n\n"
								+ "확진자 수는 " + increment_count + " 명 입니다.";
		String quick_message = location + " 지역 실시간 확진 조회";
		
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
		
		JsonObject quickReplies = new JsonObject();
		quickReplies.addProperty("label", location + " 지역 실시간 확진자 조회");
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
	
	
	
	//수정! JSON 타입 - 1. 심플메시지 2. home 버튼 return 
	//전체 누적 확진자 조회 entity return 
	@Override //실시간 확진자 조회(전체)
	public String getAllLive() {
		
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		String date = format.format(today.getTime());
		
		//select 
		LiveVO vo = chatbotMapper.getOneLive(date);
		vo.calSum();
		String getSum = String.format("%,d", vo.getSum()); 
		
		String[] loc_name = {"강원", "경기", "경남", "경북", "광주", "대구", "대전", "부산", "서울", 
				"세종", "울산", "인천", "전남", "전북", "제주", "충남", "충북"};
		int[] loc_liveCount = {vo.getGangwon(), vo.getGyeonggi(), vo.getGyeongnam(), vo.getGyeongbuk(), 
				vo.getGwangju(), vo.getDaegu(), vo.getDaejeon(), vo.getBusan(), vo.getSeoul(), vo.getSejong(), 
				vo.getUlsan(), vo.getIncheon(), vo.getJeonnam(), vo.getJeonbuk(), vo.getJeju(), vo.getChungnam(), vo.getChungbuk()};
		StringBuffer webhook_locLive = new StringBuffer();
		for(int i = 0; i < loc_name.length; i++) {
			webhook_locLive.append(loc_name[i] + " 지역 확진자 수: " + loc_liveCount[i] + " 명\n");
		}

		// {{#webhook.total_liveCount}}, {{#webhook.locName}} JSON Format
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
		
		String live_message = location + " 지역 오늘 확진자 수는" + one_count + " 명 입니다.";
		String quick_message = location + "누적 확진자 조회";
		
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
		quickReplies.addProperty("label", location + " 누적 확진자 조회");
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


	//사용 (X) 
	@Override//백신센터 전체 리스트(지역별로 분리)
	public String getAllCenter() {

		String[] location = {"강원도", "경기도", "경상남도", "경상북도", "광주광역시", "대구광역시", "대전광역시",
				 "부산광역시", "서울특별시", "울산광역시", "인천광역시", "전라남도", "전라북도", "제주특별자치도", "충청남도", "충청북도"};

		//location.length(총 16)만큼 {{#webhook.지역명}} 생성 
		ArrayList<StringBuffer> list = new ArrayList<StringBuffer>();
		
		for(int i = 0; i < location.length; i++) {
			
			StringBuffer facility_name = new StringBuffer();
			//select
			List<CenterVO> vo = chatbotMapper.getLocCenter(location[i]);
			
			for(int k = 0; k < vo.size(); k++) {
				facility_name.append(vo.get(k).getFacility_name() + "\n");		
			} 
			list.add(facility_name);			
			
		}	
		
		
		//{{#webhook.지역명}} JSON Format
		JsonObject all_center = new JsonObject();
		
		for(int i = 0; i < location.length; i++) {			
			all_center.addProperty(location[i], list.get(i).toString().replaceAll(",", ""));
		}
		
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("data", all_center);
		
		return all_center.toString();
	}

	
	@Override//백신센터 지역별(도, 시)
	public String getLocCenter(String location) {
		
		//get val location
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("vaccine_center_location").getAsString();
		
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

	
	//도, 시 정보 받아오면 처리해서 시, 군, 구 return - 각 시, 군, 구에 발화주기 
	//if문 처리 - 만약 vo.get의 사이즈가 9 이상이라면 vo.length변수 먼저 줬던 걸 9로 바꾸고 반복문 돌리기 - 그리고 그 외 지역 추가
	
	//두번째 시, 군, 구 주기 - 만약 vo.get 사이즈가 9 이상이라면, 9번째 인덱스부터 조회(i값을 9로 주면 됨) 
	//get해서 뽑아오고 센터네임을 " "기준으로 split해서 1,2번째 값 가져오기  

	
	@Override//백신센터 지역별(시, 군, 구) + 주소링크
	public String getTownCenter(String location) {
		
		//get val location
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("vaccine_center_detailLoc").getAsString();
		
		//select
		List<CenterVO> vo = chatbotMapper.getTownCenter(location);
		
		//JSON format
		//link box 형태 출력, 지역마다 센터 수가 상이하기 때문에 반복문으로 items 생성 
		JsonArray setItems_array = new JsonArray(); 
		for(int i = 0; i < vo.size(); i++) {
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
		
		JsonObject outputs = new JsonObject();
		outputs.add("outputs", outputs_array);
		
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("template", outputs);

		return result.toString();
	}

}