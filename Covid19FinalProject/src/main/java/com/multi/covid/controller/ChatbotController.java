package com.multi.covid.controller;


import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.multi.covid.domain.ResultVO;
import com.multi.covid.service.ChatbotService;

@Controller
public class ChatbotController {
	@Autowired
	private ChatbotService service;
	/*http://61.102.5.133:9091/*/
	/*http://49.142.68.213:9091*/
	
	
	//누적 확진자 조회 (전체, 지역별)
	@RequestMapping(value="/result", method=RequestMethod.POST)
	@ResponseBody
	public String covid_result(@RequestBody String location) {
		//{{#webhook.count}} return
		return service.getOneResult(location);
	}
	
	
	/*
	 * @ResponseBody
	 * @RequestMapping("/test") public String test() { 
	 * LiveVO vo = service.getOneLive("2021-05-18"); vo.calSum();
	 * 
	 * JsonObject obj = new JsonObject(); obj.addProperty("live_date",
	 * vo.getLive_date()); 
	 * obj.addProperty("sum", vo.getSum());
	 * 
	 * return obj.toString(); }
	 */
	
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
