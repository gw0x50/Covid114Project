package com.multi.covid.service;

import java.util.HashMap;
import java.util.List;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

public interface MapService {
	LiveVO getOneLive(String date);
	List<CenterVO> getAllCenter();
	List<CenterVO> getAllCentertemp();
	ResultVO getOneResult(String date);
	List<ResultVO> getBetweenResult(HashMap<String, String> map);
	List<ResultVO> getAllResult();
}
