package com.multi.covid.service;

public interface ChartService {
	public String get7DaysResult(String location); // 최근 7일간 확진자 정보 조회

	public String get4WeeksResult(String location); // 최근 4달간 확진자 정보 조회

	public String get12MonthsResult(String location); // 최근 12개월간 확진자 정보 조회
}
