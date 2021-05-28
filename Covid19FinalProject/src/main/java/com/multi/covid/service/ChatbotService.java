package com.multi.covid.service;


public interface ChatbotService {
	String getAllResult();
	String getOneResult(String location);
	String getAllLive();
	String getLocLive(String location);
	String getAllCenter();
	String getLocCenter(String location);
	String selectCenterLocation(String location);
	String selectCenterLocation2(String location);
	String getTownCenter(String location);
}
