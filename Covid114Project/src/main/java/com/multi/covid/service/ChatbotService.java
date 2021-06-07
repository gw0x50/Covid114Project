package com.multi.covid.service;

import com.google.gson.JsonArray;

public interface ChatbotService {
	String getCardJsonString(JsonArray quick_array, String title_message); // BasicCard형 JSON return
	
	String getTextJsonString(); // NullPointerException 발생 시 Text형 JSON return 

	String getResult(String location); // 누적 확진자 조회

	String getLive(String location); // 실시간 확진자 조회(전체/지역)

	String getCenterList(String location); // 백신센터 지역별(도, 시) 조회

	String selectAddr(String location); // 지역 바로가기 버튼

	String getCenterUrl(String address, int lengthNum); // 센터 주소 링크 리스트

	String getCenterUrl_over(String address); // 센터 주소 링크 리스트 (리스트 5개 초과)

	String facilityCheck(String facility_name); // 백신 센터 검색, 센터 주소 링크 리스트 

	String getKakaoMapId(String facility_name); // 카카오맵ID return
}