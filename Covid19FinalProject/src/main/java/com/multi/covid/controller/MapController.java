package com.multi.covid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.multi.covid.service.MapService;

@Controller
@RequestMapping("/map")
public class MapController {
	@Autowired
	private MapService service;
	//위도,경도 주변 백신 접종 센터 정보 조회
	@RequestMapping(value = "/getAllCenter", method = RequestMethod.POST)
	@ResponseBody
	public String getValues(String lat, String lng) {
		return service.getAllCenter(lat,lng);
	}
}