package com.multi.covid.service;

import java.text.SimpleDateFormat;
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

	@Override
	public String getOneResult(String location) {
		//SELECT * FROM covid_result WHERE result_date=#{date} and location=#{location}		
		
		//JSON Parsing - result_location 
		//location
		JsonObject jsonObj = (JsonObject) JsonParser.parseString(location);
		JsonElement action = jsonObj.get("action");
		JsonObject params = action.getAsJsonObject().get("params").getAsJsonObject();
		location = params.getAsJsonObject().get("result_location").getAsString();
		

		//date
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
				
		Calendar today = Calendar.getInstance();
		//업데이트 시간 재확인 
		if(today.get(Calendar.HOUR_OF_DAY) < 10 || today.get(Calendar.DAY_OF_WEEK) == 1) {
			today.add(Calendar.DAY_OF_MONTH, -2);
		}
		else {
			today.add(Calendar.DAY_OF_MONTH, -1);
		}
		String date = format.format(today.getTime());
		System.out.println(date);
		
		ResultVO vo = chatbotMapper.getOneResult(date, location);
		
		//webhook.count JSON
		//천단위 콤마(,)
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

	
	@Override
	public List<ResultVO> getBetweenResult(HashMap<String, String> map) {
		return chatbotMapper.getBetweenResult(map);
	}

	@Override
	public List<ResultVO> getAllResult() {
		return chatbotMapper.getAllResult();
	}

	@Override
	public LiveVO getOneLive(String date) {
		return chatbotMapper.getOneLive(date);
	}

	@Override
	public List<CenterVO> getAllCenter() {
		return chatbotMapper.getAllCenter();
	}
}
