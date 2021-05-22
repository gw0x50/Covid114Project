package com.multi.covid.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.service.AISpeakerService;

@Controller
@RequestMapping("/speaker")
public class AISpeakerController {
	@Autowired
	private AISpeakerService service;
	
	@ResponseBody
	@RequestMapping("/daily-patient")//일일확진자	
	public String test() {
		return service.DailyPatient();
	}

	@ResponseBody
	@RequestMapping("/listtest")
	public String listtest() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("startDate", "2020-05-01");
		map.put("endDate", "2020-05-10");
		List<ResultVO> resultList = service.getBetweenResult(map);
		
		JsonObject obj = new JsonObject();
		obj.addProperty("title", "list");
		
		JsonArray array = new JsonArray();
		for(ResultVO vo : resultList) {
			JsonObject inObj = new JsonObject();
			
			inObj.addProperty("result_date", vo.getResult_date());
			inObj.addProperty("location", vo.getLocation());
			inObj.addProperty("increment_count", vo.getIncrement_count());
			inObj.addProperty("total_count", vo.getTotal_count());
			
			array.add(inObj);
		}
		
		obj.add("data", array);
		
		return obj.toString();
	}
}
