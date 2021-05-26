package com.multi.covid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.multi.covid.service.MapService;

@Controller
public class WebController {
	@Autowired
	private MapService mapService;

	@RequestMapping("/")
	public ModelAndView main() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("index");

		return mv;
	}
}
