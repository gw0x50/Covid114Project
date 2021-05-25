package com.multi.covid.service;

import java.util.HashMap;
import java.util.List;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

public interface ChatbotService {
	String getOneResult(String location);
	String getAllLive();
	String getLocLive(String location);
	String getAllCenter();
	String getLocCenter(String location);
	String getTownCenter(String location);
	LiveVO getOneLive(String date);
	List<ResultVO> getAllResult();
}
