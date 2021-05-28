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

	@RequestMapping(value = "/getValues", method = RequestMethod.GET)
	@ResponseBody
	public String getValues(String type, String location) {
		String result = "";
		
		if (location.equals("전체")) location = "합계";

		if (type.equals("일별")) {
			result = service.get7DaysResult(location);
		}
		else if (type.equals("주별")) {
			result = service.get4WeeksResult(location);
		}
		else if (type.equals("월별")) {
			result = service.get12MonthsResult(location);
		}

		return result;
	}

}