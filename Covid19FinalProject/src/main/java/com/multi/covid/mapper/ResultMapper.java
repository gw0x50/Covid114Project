package com.multi.covid.mapper;

import java.util.HashMap;
import java.util.List;

import com.multi.covid.domain.ResultVO;

public interface ResultMapper {
	ResultVO getOneResult(String date);
	List<ResultVO> getBetweenResult(HashMap<String, String> map);
	List<ResultVO> getAllResult();
}
