package com.multi.covid.service;

import com.google.gson.JsonArray;

public interface ChatbotService {
	String getJsonString(JsonArray quick_array, String title_message);
	String getResult(String location);
	String getLive(String location);
	String getCenterList(String location);
	String selectAddr(String location);
	String selectAddrRemainder(String location);
	String getCenterUrl(String address, int lengthNum);
	String getCenterUrl_over10(String address);
	String getCenterUrl_over15(String address);
	String facilityCheck(String facility_name);
	String getKakaoMapId(String facility_name);
}
