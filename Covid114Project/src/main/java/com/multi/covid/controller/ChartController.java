package com.multi.covid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.multi.covid.service.ChartService;

@Controller
@RequestMapping("/chart")
public class ChartController {
	@Autowired
	private ChartService service;

	// 최근 7일간 확진자 정보 조회
	@RequestMapping(value = "/get7DaysResult", method = RequestMethod.POST)
	@ResponseBody
	public String get7DaysResult(String location) {
		return service.get7DaysResult(location);
	}

	// 최근 4주간 확진자 정보 조회
	@RequestMapping(value = "/get4WeeksResult", method = RequestMethod.POST)
	@ResponseBody
	public String get4WeeksResult(String location) {
		return service.get4WeeksResult(location);
	}

	// 최근 12개월간 확진자 정보 조회
	@RequestMapping(value = "/get12MonthsResult", method = RequestMethod.POST)
	@ResponseBody
	public String get12MonthsResult(String location) {
		return service.get12MonthsResult(location);
	}
}