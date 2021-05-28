package com.multi.covid.service;

import java.util.HashMap;
import java.util.List;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

public interface AISpeakerService {	
	LiveVO getOneLive(String date);
	List<CenterVO> getAllCenter();
	List<ResultVO> getOneResult(String date);
	List<ResultVO> getBetweenResult(HashMap<String, String> map);
	List<ResultVO> getAllResult();
	String patient(String day, String location); //일일 확진자수	
	List<String> geolocation(String r1, String r2, String r3); //가까운 진료소
	
}
