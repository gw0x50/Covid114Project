package com.multi.covid.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService{
	@Autowired
	private MapMapper mapMapper;

	@Override
	public List<CenterVO> getAllCenter(String lat,String lng) {//위도경도 주변 백신센터 DB받아오기
		double lat1 = Double.parseDouble(lat);
		double lng1 = Double.parseDouble(lng);
		return mapMapper.getAllCenter(lat1,lng1);
	}
}
