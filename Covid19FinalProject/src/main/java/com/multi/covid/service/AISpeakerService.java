package com.multi.covid.service;

import java.util.HashMap;
import java.util.List;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

public interface AISpeakerService {	
	String Patient(String day); //일일 확진자수	
	List<String> geolocation(String r1, String r2, String r3); //가까운 진료소
}
