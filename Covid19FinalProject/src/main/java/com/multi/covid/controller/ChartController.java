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
	
	// 확진자 차트 데이터 조회
	@RequestMapping(value = "/getValues", method = RequestMethod.POST)
	@ResponseBody
	public String getValues(String type, String location) {
		String result = "";
		
		if (location.equals("전체")) location = "합계"; // DB에서 사용되는 location 값으로 변경
		
		// 요청한 차트 type에 따라 알맞은 service 호출
		if (type.equals("일일")) {
			result = service.get7DaysResult(location);
		}
		else if (type.equals("주간")) {
			result = service.get4WeeksResult(location);
		}
		else if (type.equals("월간")) {
			result = service.get12MonthsResult(location);
		}

		return result;
	}

}