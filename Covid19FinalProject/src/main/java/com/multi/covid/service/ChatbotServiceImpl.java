package com.multi.covid.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

	//누적 확진자 조회 전체 추가 
	
	@Override //누적 확진자 조회(지역별)  
	public String getOneResult(String location) {
		//JSON Parsing - result_location 
		//get val location
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("result_location").getAsString();
		

		//get val date
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat wh_format = new SimpleDateFormat ("yyyy년 MM월 dd일");		
		Calendar today = Calendar.getInstance();
		
		//webhook.result_date 추가 (집계일 추가) 
		// 업데이트시간 예외처리 수정 
		String result_date = "";
		
		if(today.get(Calendar.HOUR_OF_DAY) < 11 && today.get(Calendar.MINUTE) < 15) {
			today.add(Calendar.DAY_OF_MONTH, -2);
			result_date = wh_format.format(today.getTime());
		}
		else {
			today.add(Calendar.DAY_OF_MONTH, -1);
			result_date = wh_format.format(today.getTime());
		}
		
		String date = format.format(today.getTime());
		
		//select
		ResultVO vo = chatbotMapper.getOneResult(date, location);
		
		//Skill JSON return 으로 수정 > 바로가기버튼(발화: ㅇㅇ지역 실시간 확진 조회) return 
		//{{#webhook.getTotal_count}}, {{#webhook.increment_count}} JSON Format
		//number format comma (xxx,xxx)
		String getTotal_count = String.format("%,d", vo.getTotal_count());
		String increment_count = String.format("%,d", vo.getIncrement_count());
		
		JsonObject count = new JsonObject();
		count.addProperty("count", getTotal_count);
		count.addProperty("increment_count", increment_count);
		count.addProperty("result_date", result_date);
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("data", count);
		
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
		
		// Skill JSON return으로 수정 (전체 출력) 
		// {{#webhook.total_liveCount}}, {{#webhook.locName}} JSON Format
		// 전체, 지역별 전부
		JsonObject total_liveCount = new JsonObject();
		total_liveCount.addProperty("total_liveCount", getSum);
		total_liveCount.addProperty("gangwon", vo.getGangwon());
		total_liveCount.addProperty("gyeonggi", vo.getGyeonggi());
		total_liveCount.addProperty("gyeongnam", vo.getGyeongnam());
		total_liveCount.addProperty("gyeongbuk", vo.getGyeongbuk());
		total_liveCount.addProperty("gwangju", vo.getGwangju());
		total_liveCount.addProperty("daegu", vo.getDaegu());
		total_liveCount.addProperty("daejeon", vo.getDaejeon());
		total_liveCount.addProperty("busan", vo.getBusan());
		total_liveCount.addProperty("seoul", vo.getSeoul());
		total_liveCount.addProperty("ulsan", vo.getUlsan());
		total_liveCount.addProperty("incheon", vo.getIncheon());
		total_liveCount.addProperty("jeonnam", vo.getJeonnam());
		total_liveCount.addProperty("jeonbuk", vo.getJeonbuk());
		total_liveCount.addProperty("jeju", vo.getJeju());
		total_liveCount.addProperty("chungnam", vo.getChungnam());
		total_liveCount.addProperty("chungbuk", vo.getChungbuk());
		total_liveCount.addProperty("sejong", vo.getSejong());
		
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
		
		
		//{{#webhook.one_livecount}} JSON Format
		JsonObject one_liveCount = new JsonObject();
		one_liveCount.addProperty("live_count", one_count);
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("data", one_liveCount);
		
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
		
		JsonObject header = new JsonObject();
		header.add("items", items_array);
		header.add("header", title);
		
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