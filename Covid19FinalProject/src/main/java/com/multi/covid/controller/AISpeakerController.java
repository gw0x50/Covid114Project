package com.multi.covid.controller;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.multi.covid.service.AISpeakerService;

@Controller
@RequestMapping("/speaker")
public class AISpeakerController {
	@Autowired
	private AISpeakerService service;	
	
	@ResponseBody
	@RequestMapping("/patient")//일일확진자	
	public String getPatient(String day, String location) {//(today / yesterday)
		return service.getPatient(day, location);
	}
	
	@ResponseBody
	@RequestMapping("/triage") //가까운 선별진료소
	public String getGeolocation(String r1, String r2, String r3) { // r1 - 시, r2 - 구, r3 - 동
		//스피커 접속지역의 선별진료소 목록을 반환한다
		List<String> list = service.getGeolocation(r1, r2, r3);		
		return list.get(0);
	}
	
	@ResponseBody //classpath 수정
	@RequestMapping("/weather") //특정 지역의 날씨정보
	public String getWeather(String lat, String lon) throws IOException, ParseException {
		//스피커 접속지역의 날씨정보를 반환한다		
		return service.getWeather(lat, lon);
	}	
}
