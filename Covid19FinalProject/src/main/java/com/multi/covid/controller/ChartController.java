package com.multi.covid.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.service.ChartService;

@Controller
@RequestMapping("/chart")
public class ChartController {
	@Autowired
	private ChartService service;

	@RequestMapping(value = "/getValues", method = RequestMethod.GET)
	@ResponseBody
	public String getValues(String type, String location) {
		Gson gson = new Gson();
		List<ResultVO> list = new ArrayList<ResultVO>();

		if (location.equals("전체")) location = "합계";

		if (type.equals("일별")) {
			list = service.get7DaysResult(location);
		}
		else if (type.equals("주별")) {
			list = service.get4WeeksResult(location);
		}
		else if (type.equals("월별")) {
			list = service.get12MonthsResult(location);
		}

		return gson.toJson(list);
	}

}