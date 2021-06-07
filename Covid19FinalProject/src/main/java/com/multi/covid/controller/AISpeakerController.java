package com.multi.covid.controller;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.multi.covid.service.AISpeakerService;

@Controller
@RequestMapping("/speaker")
public class AISpeakerController {
	@Autowired
	private AISpeakerService service;	
	
	@ResponseBody
	@RequestMapping(value="/patient" ,method=RequestMethod.POST) //일일확진자 및 누적확진자 조회
	public String patient(String day, String location) {
		System.out.println("day: " + day + "location: "+ location);
		return service.getPatient(day, location);
	}
	
	@ResponseBody
	@RequestMapping(value="/vaccine-center", method=RequestMethod.POST) //스피커 접속지역의 가까운 선별진료소 조회
	public String vaccineCenter(String r1, String r2, String r3) { // r1 - 시, r2 - 구, r3 - 동		
		return service.getVaccineCenter(r1, r2, r3);
	}
	
	@ResponseBody
	@RequestMapping(value="/weather", method=RequestMethod.POST) //스피커 접속지역의 날씨정보 조회
	public String weather(String lat, String lon) throws IOException, ParseException {
		return service.getWeather(lat, lon);
	}	
}
