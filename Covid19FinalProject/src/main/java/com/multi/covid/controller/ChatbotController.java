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
	
	//누적 확진자 조회
	@RequestMapping(value="/result", method=RequestMethod.POST)
	@ResponseBody
	public String getResult(@RequestBody String location) {
		return service.getResult(location);	
	}	
	
	
	//실시간 확진자 조회
	@RequestMapping(value="/live", method=RequestMethod.POST)
	@ResponseBody
	public String getLive(@RequestBody String location) {
		return service.getLive(location);
	}
	
	
	//백신 센터 리스트 
	@RequestMapping(value="/vaccinelist", method=RequestMethod.POST)
	@ResponseBody
	public String getCenterList(@RequestBody String location) {	
		return service.getCenterList(location);
	}
	

	//백신 센터 선택지 return 
	@RequestMapping(value="/selectaddr", method=RequestMethod.POST)
	@ResponseBody
	public String selectAddr(@RequestBody String location) {
		return service.selectAddr(location);		
	}
	
	//백신 센터 나머지 선택지 return 
	@RequestMapping(value="/selectremainder", method=RequestMethod.POST)
	@ResponseBody
	public String selectAddrRemainder(@RequestBody String location) {
		return service.selectAddrRemainder(location);		
	}	
				
	//백신 센터 조회(시 군 읍) + 링크
	@RequestMapping(value="/vaccineaddr", method=RequestMethod.POST)
	@ResponseBody
	public String getCenterUrl(@RequestBody String address) {
		return service.getCenterUrl(address, 1);	
	}
	
	//백신 센터 조회(시 군 읍) + 링크 (5 초과 10 이하)
	@RequestMapping(value="/vaccineaddr2", method=RequestMethod.POST)
	@ResponseBody
	public String getAddrCenter2(@RequestBody String address) {
		return service.getCenterUrl_over10(address);	
	}
	
	//백신 센터 조회(시 군 읍) + 링크 (15 이상)
	@RequestMapping(value="/vaccineaddr3", method=RequestMethod.POST)
	@ResponseBody
	public String getAddrCenter3(@RequestBody String address) {
		return service.getCenterUrl_over15(address);	
	}
	
	//기관 이름 직접 조회(주소링크 retrun)
	@RequestMapping(value="/facilitycheck", method=RequestMethod.POST)
	@ResponseBody
	public String facilityCheck(@RequestBody String facility_name) {
		return service.facilityCheck(facility_name);	
	}
	  
}