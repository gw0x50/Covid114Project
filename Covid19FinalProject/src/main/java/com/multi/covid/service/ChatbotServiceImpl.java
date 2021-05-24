package com.multi.covid.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	
	@Override //누적 확진자 조회(전체, 지역별)  
	public String getOneResult(String location) {
		//JSON Parsing - result_location 
		// get val location
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("result_location").getAsString();
		

		// get val date
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
				
		Calendar today = Calendar.getInstance();
		
		if(today.get(Calendar.HOUR_OF_DAY) < 10) {
			today.add(Calendar.DAY_OF_MONTH, -2);
		}
		else {
			today.add(Calendar.DAY_OF_MONTH, -1);
		}
		String date = format.format(today.getTime());
		
		ResultVO vo = chatbotMapper.getOneResult(date, location);
		
		// webhook JSON Format
		// number format comma (xxx,xxx)
		String getTotal_count = String.format("%,d", vo.getTotal_count());
		String increment_count = String.format("%,d", vo.getIncrement_count());
		
		JsonObject count = new JsonObject();
		count.addProperty("count", getTotal_count);
		count.addProperty("increment_count", increment_count);
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
		
		LiveVO vo = chatbotMapper.getOneLive(date);
		vo.calSum();
		String getSum = String.format("%,d", vo.getSum());
		
		// webhook JSON Format
		JsonObject total_liveCount = new JsonObject();
		total_liveCount.addProperty("total_liveCount", getSum);
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("data", total_liveCount);

		return result.toString();
		
	}


	
	@Override //실시간 확진자 조회(지역별) 
	public String getRegLive(String location) {
		
		//JSON Parsing - live_location 
		// get val location
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("live_location").getAsString();

		// get val date
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		String date = format.format(today.getTime());
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("location", location);
		map.put("date", date);
		
		int one_count = chatbotMapper.getRegLive(map);
		
		
		// webhook JSON Format
		JsonObject one_liveCount = new JsonObject();
		one_liveCount.addProperty("live_count", one_count);
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("data", one_liveCount);
		
		return result.toString();
		
	}


	@Override//백신센터 전체 리스트 
	public String getAllCenter() {
		List<CenterVO> vo = chatbotMapper.getAllCenter();
		StringBuffer facility_name = new StringBuffer();

		for(int i = 0; i < vo.size(); i++) {
			facility_name.append(vo.get(i).getFacility_name() + "\n");
		}
		
		// webhook JSON Format
		JsonObject all_center = new JsonObject();
		all_center.addProperty("facility_name", facility_name.toString().replaceAll(",", ""));
		JsonObject result = new JsonObject();
		result.addProperty("version", "2.0");
		result.add("data", all_center);
		
		return all_center.toString();
	}

	
	
	
	@Override
	public LiveVO getOneLive(String date) {
		return chatbotMapper.getOneLive(date);
	}


	@Override
	public List<ResultVO> getAllResult() {
		return chatbotMapper.getAllResult();
	}


}