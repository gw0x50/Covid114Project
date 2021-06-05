package com.multi.covid.service;

import com.google.gson.JsonArray;

public interface ChatbotService {
	String getJsonString(JsonArray quick_array, String title_message); // BasicCard형 JSON return
	String getResult(String location); // 누적 확진자 조회
	String getLive(String location); // 실시간 확진자 조회(전체/지역)
	String getCenterList(String location); // 백신센터 지역별(도, 시)
	String selectAddr(String location); // 지역 바로가기 버튼(최대 20)
	String selectAddrRemainder(String location); // 20개 초과 지역 버튼 처리
	String getCenterUrl(String address, int lengthNum); // 센터주소 링크 리스트
	String getCenterUrl_over(String address); // 리스트 5개 초과
	String facilityCheck(String facility_name); // 백신 센터 직접 검색 
	String getKakaoMapId(String facility_name); // 카카오맵ID return
}
