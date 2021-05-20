package com.multi.covid.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.ChartMapper;

@Service
public class ChartServiceImpl implements ChartService {
	@Autowired
	private ChartMapper chartMapper;

	@Override
	public ResultVO getOneResult(String date) {
		return chartMapper.getOneResult(date);
	}

	@Override
	public List<ResultVO> getBetweenResult(HashMap<String, String> map) {
		return chartMapper.getBetweenResult(map);
	}

	@Override
	public List<ResultVO> getAllResult() {
		return chartMapper.getAllResult();
	}

	@Override
	public LiveVO getOneLive(String date) {
		return chartMapper.getOneLive(date);
	}

	@Override
	public List<CenterVO> getAllCenter() {
		return chartMapper.getAllCenter();
	}
}
