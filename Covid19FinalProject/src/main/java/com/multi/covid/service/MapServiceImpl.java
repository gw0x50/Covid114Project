package com.multi.covid.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.multi.covid.domain.CenterVO;
import com.multi.covid.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService {
	@Autowired
	private MapMapper mapper;

	// 위도, 경도 주변 백신 접종 센터 정보 조회
	@Override
	public String getLocalCenter(String lat, String lng) {
		Gson gson = new Gson();
		
		// 받아온 위도, 경도 Type변경 (String -> double)
		double doubleLat = Double.parseDouble(lat);
		double doubleLng = Double.parseDouble(lng);
		
		List<CenterVO> allCenter = mapper.getLocalCenter(doubleLat, doubleLng);// sql 호출 및 응답 정보 저장
		
		return gson.toJson(allCenter);
	}
}
