package com.multi.covid.service;

public interface MapService {
	String getLocalCenter(String lat, String lng); // 위도, 경도 주변 백신 접종 센터 정보 조회
}