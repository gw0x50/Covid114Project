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
	//	System.out.println("확인" + service.getAllCenter());
		return"";
	}
	
	
	//누적 확진자 조회 (전체, 지역별)
	@RequestMapping(value="/result", method=RequestMethod.POST)
	@ResponseBody
	public String getOneResult(@RequestBody String location) {
		//{{#webhook.count}} return
		//{{#webhook.increment_count}} return
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
	public String getLocLive(@RequestBody String location) {
		//{{#webhook.total_OneCount}} return
		return service.getLocLive(location);
	}
	
	//백신 센터 조회(전체)
	@RequestMapping(value="/vaccineall", method=RequestMethod.POST)
	@ResponseBody
	public String getAllCenter() {
		//{{#webhook.facility_name}} return
		return service.getAllCenter();
	}
	
	//백신 센터 조회(지역별)
	@RequestMapping(value="/vaccineloc", method=RequestMethod.POST)
	@ResponseBody
	public String getLocCenter(@RequestBody String location) {
		//{{#webhook.facility_name}} return
		return service.getLocCenter(location);
	}
	
	
	//백신 센터 조회(지역 - 시/군별)
	@RequestMapping(value="/vaccinetown", method=RequestMethod.POST)
	@ResponseBody
	public String getTownCenter(@RequestBody String location) {
		//{{#webhook.facility_name}}, {{webhook.location_url}} return
		/*
		 * System.out.println(service.getTownCenter(location)); return
		 * service.getTownCenter(location);
		 */
		
		String test = "{\r\n"
				+ "  \"version\": \"2.0\",\r\n"
				+ "  \"template\": {\r\n"
				+ "    \"outputs\": [\r\n"
				+ "      {\r\n"
				+ "        \"listCard\": {\r\n"
				+ "          \"header\": {\r\n"
				+ "            \"title\": \"카카오 i 디벨로퍼스를 소개합니다\"\r\n"
				+ "          },\r\n"
				+ "          \"items\": [\r\n"
				+ "            {\r\n"
				+ "              \"title\": \"Kakao i Developers\",\r\n"
				+ "              \"description\": \"새로운 AI의 내일과 일상의 변화\",\r\n"
				+ "              \"imageUrl\": \"http://k.kakaocdn.net/dn/APR96/btqqH7zLanY/kD5mIPX7TdD2NAxgP29cC0/1x1.jpg\",\r\n"
				+ "              \"link\": {\r\n"
				+ "                \"web\": \"https://namu.wiki/w/%EB%9D%BC%EC%9D%B4%EC%96%B8(%EC%B9%B4%EC%B9%B4%EC%98%A4%ED%94%84%EB%A0%8C%EC%A6%88)\"\r\n"
				+ "              }\r\n"
				+ "            },\r\n"
				+ "            {\r\n"
				+ "              \"title\": \"Kakao i Open Builder\",\r\n"
				+ "              \"description\": \"카카오톡 채널 챗봇 만들기\",\r\n"
				+ "              \"imageUrl\": \"http://k.kakaocdn.net/dn/N4Epz/btqqHCfF5II/a3kMRckYml1NLPEo7nqTmK/1x1.jpg\",\r\n"
				+ "              \"link\": {\r\n"
				+ "                \"web\": \"https://namu.wiki/w/%EB%AC%B4%EC%A7%80(%EC%B9%B4%EC%B9%B4%EC%98%A4%ED%94%84%EB%A0%8C%EC%A6%88)\"\r\n"
				+ "              }\r\n"
				+ "            },\r\n"
				+ "            {\r\n"
				+ "              \"title\": \"Kakao i Voice Service\",\r\n"
				+ "              \"description\": \"보이스봇 / KVS 제휴 신청하기\",\r\n"
				+ "              \"imageUrl\": \"http://k.kakaocdn.net/dn/bE8AKO/btqqFHI6vDQ/mWZGNbLIOlTv3oVF1gzXKK/1x1.jpg\",\r\n"
				+ "              \"link\": {\r\n"
				+ "                \"web\": \"https://namu.wiki/w/%EC%96%B4%ED%94%BC%EC%B9%98\"\r\n"
				+ "              }\r\n"
				+ "            }\r\n"
				+ "          ],\r\n"
				+ "          \"buttons\": [\r\n"
				+ "            {\r\n"
				+ "              \"label\": \"구경가기\",\r\n"
				+ "              \"action\": \"webLink\",\r\n"
				+ "              \"webLinkUrl\": \"https://namu.wiki/w/%EC%B9%B4%EC%B9%B4%EC%98%A4%ED%94%84%EB%A0%8C%EC%A6%88\"\r\n"
				+ "            }\r\n"
				+ "          ]\r\n"
				+ "        }\r\n"
				+ "      }\r\n"
				+ "    ]\r\n"
				+ "  }\r\n"
				+ "}";
		return test;
	}
	
	
	
	  
}