package com.multi.covid.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	private AISpeakerService service;

	@Autowired
	private AISpeakerMapper mapper;
	
	@Override
	public List<ResultVO> getOneResult(String date) {
		return mapper.getOneResult(date);
	}

	@Override
	public List<ResultVO> getBetweenResult(HashMap<String, String> map) {
		return mapper.getBetweenResult(map);
	}

	@Override
	public List<ResultVO> getAllResult() {
		return mapper.getAllResult();
	}

	@Override
	public LiveVO getOneLive(String date) {
		return mapper.getOneLive(date);
	}

	@Override
	public List<CenterVO> getAllCenter() {
		return mapper.getAllCenter();
	}

	@Override
	public String patient(String day, String location) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String date;
		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd ");
		LiveVO vo = null;
		List<ResultVO> list = null;
		int sum=0;
		JsonObject obj = new JsonObject();
		//covid_live
		String[] loc = {"seoul", "incheon", "gwangju", "daejeon", "daegu", "busan", "ulsan", "sejong",
						"gyeonggi", "gangwon", "chungbuk", "chungnam", "jeonbuk", "jeonnam", "gyeongbuk", 
						"gyeongnam", "jeju"};
		//covid_result
		String[] loc2 = {"서울", "인천", "광주", "대전", "대구", "부산", "울산", "세종", "경기", "강원", "충북", "충남", 
						"전북", "전남", "경북", "경남", "제주"};
		if(day.equals("today")) {
			try {
			date = format.format(cal.getTime());
			System.out.println("오늘: "+date);			
			vo = service.getOneLive(date); //오늘날짜 확진자수 받아오기
			vo.calSum();
			obj.addProperty("live_date", vo.getLive_date());
			if(location.equals("all")) { //전체지역 확진자								
				obj.addProperty("sum", vo.getSum());
			}
			else if(Arrays.asList(loc).contains(location)) {//location값이 배열에 있는지 확인(있으면 true 반환) 				
				obj.addProperty("sum", vo.getLocation(location));//특정지역 확진자수 조회
			}
			else {
				obj.addProperty("sum", "잘못된접근");
			}
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
			list = service.getOneResult(date); //어제날짜 확진자수 받아오기
			System.out.println(list.get(0).getTotal_count());//데이터가 없으면 에러발생
			
			if(location.equals("all")) {
				for(ResultVO one : list) {
					if(one.getLocation().equals("합계")) 
						sum = one.getIncrement_count();//모든지역 확진자수				
				}
			}
			else if(Arrays.asList(loc2).contains(location)) {
				for(ResultVO one : list) {
					if(one.getLocation().equals(location)) 
						sum = one.getIncrement_count();//특정지역 확진자수				
				}
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
					if(one.getLocation().equals("합계")) {
						System.out.println(one.getLocation());
						sum += one.getIncrement_count();
					}
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
