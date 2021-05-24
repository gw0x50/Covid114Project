package com.multi.covid.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.multi.covid.mapper.MapMapper;

@Service
public class AISpeakerServiceImpl implements AISpeakerService {
	@Autowired
	private MapMapper mapMapper;

	@Autowired
	private AISpeakerService service;
	
	@Autowired
	private AISpeakerMapper mapper;
	
	@Override
	public ResultVO getOneResult(String date) {
		return mapMapper.getOneResult(date);
	}

	@Override
	public List<ResultVO> getBetweenResult(HashMap<String, String> map) {
		return mapMapper.getBetweenResult(map);
	}

	@Override
	public List<ResultVO> getAllResult() {
		return mapMapper.getAllResult();
	}

	@Override
	public LiveVO getOneLive(String date) {
		return mapMapper.getOneLive(date);
	}

	@Override
	public List<CenterVO> getAllCenter() {
		return mapMapper.getAllCenter();
	}

	@Override
	public String DailyPatient() {
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd ");
		String today = format.format(time);
//		System.out.println("오늘: "+today);
		LiveVO vo = service.getOneLive("2021-05-23"); //오늘날짜 확진자수 받아오기
		vo.calSum();				
		
		JsonObject obj = new JsonObject();
		obj.addProperty("live_date", vo.getLive_date());
		obj.addProperty("sum", vo.getSum());
		 
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
