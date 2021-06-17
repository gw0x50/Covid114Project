package com.multi.covid.service;

import java.io.IOException;

import org.json.simple.parser.ParseException;

public interface AISpeakerService {
	String getPatient(String day, String location); // 일일 확진자 수	

	String getVaccineCenter(String r1, String r2, String r3); // 가까운 진료소

	String getWeather(String lat, String lon) throws IOException, ParseException; // 스피커 접속지역의 날씨 정보	
}