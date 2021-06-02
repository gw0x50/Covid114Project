package com.multi.covid.service;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

public interface AISpeakerService {	
	LiveVO getOneLive(String date);
	List<CenterVO> getAllCenter();
	List<ResultVO> getOneResult(String date);	
	List<ResultVO> getAllResult();
	String getPatient(String day, String location); //일일 확진자수	
	List<String> getGeolocation(String r1, String r2, String r3); //가까운 진료소
	String getWeather(String lat, String lon) throws IOException, ParseException; //스피커 접속지역의 날씨정보	
	
}
