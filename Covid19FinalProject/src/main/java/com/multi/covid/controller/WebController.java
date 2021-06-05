package com.multi.covid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {

	@RequestMapping("/")
	public ModelAndView main() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("index");

		return mv;
	}
}
