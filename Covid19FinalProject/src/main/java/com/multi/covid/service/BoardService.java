package com.multi.covid.service;

public interface BoardService {
	String getRecentLive(String location); // 최근 실시간 확진자 정보 조회

	String getRecentTwoResult(String location); // 최근 이틀간 확진자 정보 조회
}
