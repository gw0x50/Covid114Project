package com.multi.covid.service;

import java.util.HashMap;
import java.util.List;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

public interface MapService {
	LiveVO getOneLive(String date);
	List<CenterVO> getAllCenter(String lat,String lng);
	List<CenterVO> getAllCentertemp(String lat,String lng);
	ResultVO getOneResult(String date);
	List<ResultVO> getBetweenResult(HashMap<String, String> map);
	List<ResultVO> getAllResult();
}
