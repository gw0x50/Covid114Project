package com.multi.covid.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.ResultMapper;

public class ResultServiceImpl implements ResultService{
	@Autowired
	private ResultMapper resultMapper;
	
	public void setResultMapper(ResultMapper resultMapper) {
		this.resultMapper = resultMapper;
	}
	
	@Override
	public ResultVO getOneResult(String date) {
		return resultMapper.getOneResult(date);
	}

	@Override
	public List<ResultVO> getBetweenResult(HashMap<String, String> map) {
		return resultMapper.getBetweenResult(map);
	}

	@Override
	public List<ResultVO> getAllResult() {
		return resultMapper.getAllResult();
	}

}
