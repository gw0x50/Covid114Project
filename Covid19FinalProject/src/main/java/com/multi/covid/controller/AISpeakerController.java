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

	@RequestMapping(value = "/getPatient", method = RequestMethod.POST) // 일일 확진자 및 누적 확진자 조회
	@ResponseBody
	public String getPatient(String day, String location) {
		return service.getPatient(day, location);
	}

	@RequestMapping(value = "/getVaccineCenter", method = RequestMethod.POST) // 스피커 접속지역의 가까운 선별진료소 조회
	@ResponseBody
	public String getVaccineCenter(String r1, String r2, String r3) { // r1 - 시, r2 - 구, r3 - 동		
		return service.getVaccineCenter(r1, r2, r3);
	}

	@RequestMapping(value = "/getWeather", method = RequestMethod.POST) // 스피커 접속지역의 날씨 정보 조회
	@ResponseBody
	public String getWeather(String lat, String lon) throws IOException, ParseException {
		return service.getWeather(lat, lon);
	}
}