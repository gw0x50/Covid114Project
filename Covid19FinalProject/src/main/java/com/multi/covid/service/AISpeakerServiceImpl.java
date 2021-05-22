package com.multi.covid.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
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
		LiveVO vo = service.getOneLive(today); //오늘날짜 확진자수 받아오기
		vo.calSum();				
		
		JsonObject obj = new JsonObject();
		obj.addProperty("live_date", vo.getLive_date());
		obj.addProperty("sum", vo.getSum());
		 
		return obj.toString();
	}
}
