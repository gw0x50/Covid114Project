package com.multi.covid.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.multi.covid.mapper.AISpeakerMapper;
import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

@Service
public class AISpeakerServiceImpl implements AISpeakerService {	
	
	@Autowired
	private AISpeakerMapper mapper;

	@Override
	public String Patient(String day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String date;
		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd ");
		LiveVO vo = null;
		List<ResultVO> list = null;
		int sum=0;
		JsonObject obj = new JsonObject();
		if(day.equals("today")) {	
			try {
			date = format.format(cal.getTime());
			System.out.println("오늘: "+date);			
			vo = mapper.getOneLive(date); //오늘날짜 확진자수 받아오기
			vo.calSum();
			obj.addProperty("live_date", vo.getLive_date());
			obj.addProperty("sum", vo.getSum());
			}catch(Exception e) {
				System.out.println(e.toString());
				return "결과값이 없습니다";
			}
		}
		else if(day.equals("yesterday")) {
			try {
			cal.add(Calendar.DATE, - 1);		
			date = format.format(cal.getTime());
			System.out.println("어제: "+date);
			list = mapper.getOneResult(date); //어제날짜 확진자수 받아오기
			System.out.println(list.get(0).getTotal_count());//데이터가 없으면 에러발생
			
			for(ResultVO one : list) {			
				sum += one.getIncrement_count();//각 지역별 확진자수 더하기				
			}
			obj.addProperty("result_date", date);
			obj.addProperty("sum", sum);
			}catch(Exception e) {//전날 데이터가 아직 갱신되지 않은경우
				System.out.println(e.toString());
				cal.add(Calendar.DATE, - 2);
				date = format.format(cal.getTime());
				System.out.println("그제: "+date);
				list = mapper.getOneResult(date); //그제날짜 확진자수 받아오기
				for(ResultVO one : list) {				
					sum += one.getIncrement_count();
				}
				obj.addProperty("result_date", date);
				obj.addProperty("sum", sum);					
			}				
		}
		else {
			return "잘못된접근";
		}
				
		return obj.toString();
	}		

	@Override
	public List<String> geolocation(String r1, String r2, String r3) {
		
		System.out.println("r1: "+r1+" r2: "+r2+" r3: "+r3);//ai스피커 접속지역
		List<CenterVO> vo = mapper.getAllCenter();				
		String si = (r1); //시
		String gu = (r2); //구
		String dong = (r3); //동
		String temp = "";
		List<String> result = new ArrayList<String>();
		boolean check = false;
		
		//가까운 선별진료소 파악
		for(CenterVO one : vo) {
			temp = one.getAddress();
			if(temp.contains(dong)) { //동 단위로 파악
				check = true;
				result.add(one.getFacility_name());
			}
		}
		if(!check) { //동에 선별진료소가 없는경우
			for(CenterVO one : vo) {
				temp = one.getAddress();
				if(temp.contains(gu)) { //구 단위로 파악
					check = true;
					result.add(one.getFacility_name());
				}
			}
		}
		else if(!check) { //구에 선별진료소가 없는경우
			for(CenterVO one : vo) {
				temp = one.getAddress();
				if(temp.contains(si)) { //시 단위로 파악
					check = true;
					result.add(one.getFacility_name());					
				}
			}
		}
		else { //주변에 선별진료소가 없는경우
			result.add("가까운 선별진료소가 없습니다");
		}
		
		System.out.println(check);
		for(String one : result) {
			System.out.println(one);
		}
		
		
		return result;
	}

	
}
