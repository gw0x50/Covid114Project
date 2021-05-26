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

	@RequestMapping("/charttest")
	public ModelAndView charttest() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("charttest");

		// LiveVO liveVO =  liveService.getOneLive("2021-05-18");
		// liveVO.calSum();
		// System.out.println(liveVO);

		// List<CenterVO> centerList = centerService.getAllCenter();
		// for(CenterVO a : centerList) System.out.println(a);

		List<ResultVO> sevenDays = service.get7DaysResult("합계");
		List<ResultVO> fourWeeks = service.get4WeeksResult("합계");
		List<ResultVO> twelveMonths = service.get12MonthsResult("합계");
		System.out.println(sevenDays);
		System.out.println(fourWeeks);
		System.out.println(twelveMonths);

		mv.addObject("sevenDays", sevenDays);
		mv.addObject("fourWeeks", fourWeeks);
		mv.addObject("twelveMonths", twelveMonths);

		return mv;
	}

	@RequestMapping(value = "/getValues", method = RequestMethod.GET)
	@ResponseBody
	public String getValues(String type, String location) {
		Gson gson = new Gson();
		List<ResultVO> list = new ArrayList<ResultVO>();
		
		if(location.equals("전체")) location = "합계";
		
		if (type.equals("일별")) {
			list = service.get7DaysResult(location);
		}
		if (type.equals("주별")) {
			list = service.get4WeeksResult(location);
		}
		if (type.equals("월별")) {
			list = service.get12MonthsResult(location);
		}

		return gson.toJson(list);
	}

}