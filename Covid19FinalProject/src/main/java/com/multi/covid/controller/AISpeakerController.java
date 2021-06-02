package com.multi.covid.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.AISpeakerMapper;
import com.multi.covid.service.AISpeakerService;

@Controller
@RequestMapping("/speaker")
public class AISpeakerController {
	@Autowired
	private AISpeakerService service;	
	
	@Autowired
	private AISpeakerMapper mapper;
	
	@ResponseBody
	@RequestMapping("/patient")//일일확진자	
	public String patient(String day, String location) {//(today / yesterday)
		return service.patient(day, location);
	}
	
	@ResponseBody
	@RequestMapping("/triage") //가까운 선별진료소
	public String geolocation(String r1, String r2, String r3) { // r1 - 시, r2 - 구, r3 - 동
		//스피커 접속지역의 선별진료소 목록을 반환한다
		List<String> list = service.geolocation(r1, r2, r3);		
		return list.get(0);
	}
	
	@ResponseBody //classpath 수정
	@RequestMapping("/weather") //특정 지역의 날씨정보
	public String weather(String lat, String lon) throws IOException, ParseException {
		//스피커 접속지역의 날씨정보를 반환한다		
		return service.weather(lat, lon);
	}	
}
