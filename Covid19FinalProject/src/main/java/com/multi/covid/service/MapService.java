package com.multi.covid.service;

import java.util.List;

import com.multi.covid.domain.CenterVO;

public interface MapService {
	List<CenterVO> getAllCenter(String lat,String lng);
}
