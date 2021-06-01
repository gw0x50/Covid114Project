package com.multi.covid.service;


public interface ChatbotService {
	String getAllResult();
	String getOneResult(String location);
	String getAllLive();
	String getLocLive(String location);
	String getLocCenter(String location);
	String selectAddrTwo(String location);
	String selectAddrRemainder(String location);
	String selectAddrThree(String location);
	String getCenterUrl(String location, int lengthNum);
	String getCenterUrl_over10(String location);
	String getCenterUrl_over15(String location);
	String facilityCheck(String facility_name);
	String getKakaoAddress(String facility_name);
}
