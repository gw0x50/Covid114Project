package com.multi.covid.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.multi.covid.domain.CenterVO;
import com.multi.covid.service.MapService;

@Controller
@RequestMapping("/map")
public class MapController {
	@Autowired
	private MapService mapService;

	@RequestMapping(value = "/getAllCentertemp", method = RequestMethod.GET)
	@ResponseBody
	public String getValues(String type, String location) {
		Gson gson = new Gson();
		
		List<CenterVO> allcenter = mapService.getAllCentertemp();
		return gson.toJson(allcenter);
	}

}