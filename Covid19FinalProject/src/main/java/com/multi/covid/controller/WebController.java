package com.multi.covid.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.ResultMapper;
import com.multi.covid.service.CenterService;
import com.multi.covid.service.LiveService;
import com.multi.covid.service.ResultService;

@Controller
public class WebController {
	@Autowired
	private LiveService liveService;
	@Autowired
	private CenterService centerService;
	@Autowired
	private ResultMapper ResultService;
	
	@RequestMapping("/")
	public ModelAndView main() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("index");
		
		return mv;
	}
	
	@RequestMapping("/test")
	public void test() {
		// LiveVO liveVO =  liveService.getOneLive("2021-05-18");
		// liveVO.calSum();
		// System.out.println(liveVO);
		
		// List<CenterVO> centerList = centerService.getAllCenter();
		// for(CenterVO a : centerList) System.out.println(a);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("startDate", "2020-05-01");
		map.put("endDate", "2020-05-10");
		List<ResultVO> resultList = ResultService.getBetweenResult(map);
		for(ResultVO a : resultList) System.out.println(a);
	}
}
