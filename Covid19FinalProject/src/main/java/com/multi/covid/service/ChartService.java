package com.multi.covid.service;

public interface ChartService {
	public String get7DaysResult(String location);
	public String get4WeeksResult(String location);
	public String get12MonthsResult(String location);
}
