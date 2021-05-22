package com.multi.covid.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.multi.covid.domain.ResultVO;
import com.multi.covid.service.ChartService;
import com.multi.covid.service.MapService;

@Controller
public class WebController {
	@Autowired
	private ChartService chartService;
	@Autowired
	private MapService mapService;

	@RequestMapping("/")
	public ModelAndView main() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("index");

		return mv;
	}

	@RequestMapping("/charttest")
	public ModelAndView charttest() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("charttest");

		// LiveVO liveVO =  liveService.getOneLive("2021-05-18");
		// liveVO.calSum();
		// System.out.println(liveVO);

		// List<CenterVO> centerList = centerService.getAllCenter();
		// for(CenterVO a : centerList) System.out.println(a);

		// 합계, 충북, 충남, 제주, 전북, 전남, 인천, 울산, 세종, 서울,
		// 부산, 대전, 대구, 광주, 경북, 경남, 경기, 검역, 강원 (총 19지역)
		// ArrayList<ArrayList<ResultVO>> sevenDays = chartService.get7DaysResult();
		// ArrayList<ArrayList<ResultVO>> fourWeeks = chartService.get4WeeksResult();
		
		//mv.addObject("sevenDays", sevenDays);		
		
		return mv;
	}
}
