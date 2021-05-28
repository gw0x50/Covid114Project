package com.multi.covid.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService{
	@Autowired
	private MapMapper mapMapper;

	@Override
	public ResultVO getOneResult(String date) {
		return mapMapper.getOneResult(date);
	}

	@Override
	public List<ResultVO> getBetweenResult(HashMap<String, String> map) {
		return mapMapper.getBetweenResult(map);
	}

	@Override
	public List<ResultVO> getAllResult() {
		return mapMapper.getAllResult();
	}

	@Override
	public LiveVO getOneLive(String date) {
		return mapMapper.getOneLive(date);
	}

	@Override
	public List<CenterVO> getAllCenter() {
		return mapMapper.getAllCenter();
	}

	@Override
	public List<CenterVO> getAllCentertemp() {

		return mapMapper.getAllCentertemp();
	}
}
