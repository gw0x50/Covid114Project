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
	String getAddrCenter(String location, int lengthNum);
	String getAddrCenter_2(String location);
	String getAddrCenter_3(String location);
	String facilityCheck(String facility_name);
	String getKakaoAddress(String phone, String facility_name);
}
