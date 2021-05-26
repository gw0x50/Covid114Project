package com.multi.covid.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.service.MapService;

@Controller
public class WebController {
	@Autowired
	private MapService mapService;

	@RequestMapping("/")
	public ModelAndView main() {
		ModelAndView mv = new ModelAndView();
		List<CenterVO> allcenter = mapService.getAllCenter();
		mv.addObject("allcenter",allcenter);

		mv.setViewName("index");

		return mv;
	}
}
