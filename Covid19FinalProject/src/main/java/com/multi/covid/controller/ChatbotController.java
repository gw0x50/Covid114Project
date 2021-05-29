package com.multi.covid.controller;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.multi.covid.service.ChatbotService;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {
	@Autowired
	private ChatbotService service;
	/*http://61.102.5.133:9091/*/
	/*http://49.142.68.213:9091*/
	
	//누적 확진자 전체 조회
	@RequestMapping(value="/resultall", method=RequestMethod.POST)
	@ResponseBody
	public String getAllResult() {
		return service.getAllResult();
	}
	
	//누적 확진자 조회 (전체, 지역별)
	@RequestMapping(value="/result", method=RequestMethod.POST)
	@ResponseBody
	public String getOneResult(@RequestBody String location) {
		//{{#webhook.getTotal_count}}
		//{{#webhook.increment_count}}
		//{{#weobhook.result_date}}
		System.out.println(service.getOneResult(location));
		return service.getOneResult(location);
		
		
	}
	
	//실시간 확진자 조회 (전체)
	@RequestMapping(value="/liveall", method=RequestMethod.POST)
	@ResponseBody
	public String getAllLive() {
		//{{#webhook.total_liveCount}}
		//{{#webhook.locName}}(총 17)
		return service.getAllLive();
	}
	
	//실시간 확진자 조회 (지역별)
	@RequestMapping(value="/liveone", method=RequestMethod.POST)
	@ResponseBody
	public String getLocLive(@RequestBody String location) {
		//{{#webhook.live_count}
		return service.getLocLive(location);
	}
	
	
	//백신 센터 조회(지역별)
	@RequestMapping(value="/vaccineloc", method=RequestMethod.POST)
	@ResponseBody
	public String getLocCenter(@RequestBody String location) {
		
		return service.getLocCenter(location);
	}
	
	//백신 센터 선택지 return 
	@RequestMapping(value="/selectaddrtwo", method=RequestMethod.POST)
	@ResponseBody
	public String selectAddTwo(@RequestBody String location) {
		//link list JSON return
		return service.selectAddrTwo(location);		
	}
	
	
	//백신 센터 선택지 return 
	@RequestMapping(value="/selectremainder", method=RequestMethod.POST)
	@ResponseBody
	public String selectAddrRemainder(@RequestBody String location) {
		return service.selectAddrRemainder(location);		
	}
		

	//백신 센터 선택지 return 
	@RequestMapping(value="/selectaddrthree", method=RequestMethod.POST)
	@ResponseBody
	public String selectAddThree(@RequestBody String location) {
		return service.selectAddrThree(location);		
	}
	
				
	//백신 센터 조회(지역 - 시/군별) + 링크
	@RequestMapping(value="/vaccineaddr", method=RequestMethod.POST)
	@ResponseBody
	public String getAddrCenter(@RequestBody String location) {
		//link list JSON return
		return service.getAddrCenter(location);		
	}
	
	//백신 센터 조회(지역 - 시/군별) + 링크
	@RequestMapping(value="/vaccineaddr2", method=RequestMethod.POST)
	@ResponseBody
	public String getAddrCenter2(@RequestBody String location) {
		//link list JSON return
		return service.getAddrCenter(location);	
	}
	
	
	
	  
}