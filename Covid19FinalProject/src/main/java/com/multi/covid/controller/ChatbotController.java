package com.multi.covid.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.multi.covid.service.ChatbotService;

@Controller
public class ChatbotController {
	@Autowired
	private ChatbotService service;
	/*http://61.102.5.133:9091/*/
	/*http://49.142.68.213:9091*/
	
	@RequestMapping(value="/chattest", method=RequestMethod.POST)
	@ResponseBody
	public String actiontest(@RequestBody String location) {
		//{{#webhook.count}} return
	//	System.out.println("확인" + service.getAllCenter());
		return"";
	}
	
	
	//누적 확진자 조회 (전체, 지역별)
	@RequestMapping(value="/result", method=RequestMethod.POST)
	@ResponseBody
	public String getOneResult(@RequestBody String location) {
		//{{#webhook.count}} return
		return service.getOneResult(location);
		
	}
	
	//실시간 확진자 조회 (전체)
	@RequestMapping(value="/liveall", method=RequestMethod.POST)
	@ResponseBody
	public String getAllLive() {
		//{{#webhook.total_liveCount}} return
		return service.getAllLive();
	}
	
	//실시간 확진자 조회 (지역별)
	@RequestMapping(value="/liveone", method=RequestMethod.POST)
	@ResponseBody
	public String getRegLive(@RequestBody String location) {
		//{{#webhook.total_OneCount}} return
		return service.getRegLive(location);
	}
	
	//백신 센터 조회(전체)
	@RequestMapping(value="/vaccineall", method=RequestMethod.POST)
	@ResponseBody
	public String getAllCenter() {
		//{{#webhook.facility_name}} return
		return service.getAllCenter();
	}
	
	//백신 센터 조회(지역별)
	
	
	
	//백신 센터 조회(지역 - 센터별) 
	//return null인 경우 안내문구 출력 
	
	
	  
}