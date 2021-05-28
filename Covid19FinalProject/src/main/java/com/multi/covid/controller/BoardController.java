package com.multi.covid.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService service;

	@RequestMapping(value = "/getLiveValue", method = RequestMethod.GET)
	@ResponseBody
	public String getLiveValue(String location) {	
		return service.getRecentLive(location);
	}
	
	@RequestMapping(value = "/getResultValue", method = RequestMethod.GET)
	@ResponseBody
	public String getResultValue(String location) {	
		return service.getRecentTwoResult(location);
	}


}