package com.multi.covid.controller;

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
import com.multi.covid.mapper.ResultMapper;
import com.multi.covid.service.CenterService;
import com.multi.covid.service.LiveService;

@Controller
@RequestMapping("/speaker")
public class AISpeakerController {
	@Autowired
	private LiveService liveService;
	@Autowired
	private CenterService centerService;
	@Autowired
	private ResultMapper ResultService;
	
	@ResponseBody
	@RequestMapping("/test")
	public String test() {
		LiveVO vo = liveService.getOneLive("2021-05-18");
		vo.calSum();
		
		JsonObject obj = new JsonObject();
		obj.addProperty("live_date", vo.getLive_date());
		obj.addProperty("sum", vo.getSum());
		
		return obj.toString();
	}
	
	//test
	@ResponseBody
	@RequestMapping("/listtest")
	public String listtest() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("startDate", "2020-05-01");
		map.put("endDate", "2020-05-10");
		List<ResultVO> resultList = ResultService.getBetweenResult(map);
		
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
