package com.multi.covid.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	//백신 센터 조회(전체)
	@RequestMapping(value="/vaccineall", method=RequestMethod.POST)
	@ResponseBody
	public String getAllCenter() {
		//{{#webhook.지역이름}}(총 16)
		return service.getAllCenter();
	}
	
	//백신 센터 조회(지역별)
	@RequestMapping(value="/vaccineloc", method=RequestMethod.POST)
	@ResponseBody
	public String getLocCenter(@RequestBody String location) {
		//{{#webhook.r_facility_name}}
		return service.getLocCenter(location);
	}
	
	//백신 센터 선택지 return 
	@RequestMapping(value="/vaccineselect", method=RequestMethod.POST)
	@ResponseBody
	public String selectCenterLocation(@RequestBody String location) {
		//link list JSON return
		return service.selectCenterLocation(location);		
	}
	
	
	//백신 센터 선택지 return 
		@RequestMapping(value="/vaccineselect2", method=RequestMethod.POST)
		@ResponseBody
		public String selectCenterLocation2(@RequestBody String location) {
			return service.selectCenterLocation(location);		
		}
	
	
	//백신 센터 조회(지역 - 시/군별) + 링크
	@RequestMapping(value="/vaccinetown", method=RequestMethod.POST)
	@ResponseBody
	public String getTownCenter(@RequestBody String location) {
		//link list JSON return
		return service.getTownCenter(location);		
	}
	
	
	
	  
}