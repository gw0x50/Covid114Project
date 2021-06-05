package com.multi.covid.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.multi.covid.domain.CenterVO;
import com.multi.covid.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService{
	@Autowired
	private MapMapper mapper;
	
	//위도,경도 주변 백신 접종 센터 정보 조회
	@Override
	public String getAllCenter(String lat,String lng) {
		//받아온 위도 경도 Type변경 (String -> double)
		double lat1 = Double.parseDouble(lat);
		double lng1 = Double.parseDouble(lng);
		Gson gson = new Gson();
		List<CenterVO> allcenter = mapper.getAllCenter(lat1, lng1);// sql 호출 및 응답 정보 저장
		return gson.toJson(allcenter);
	}
}
