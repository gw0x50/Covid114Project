package com.multi.covid.service;

import java.util.HashMap;
import java.util.List;

import com.multi.covid.domain.ResultVO;

public interface ResultService{
	ResultVO getOneResult(String date);
	List<ResultVO> getBetweenResult(HashMap<String, String> map);
	List<ResultVO> getAllResult();
}
