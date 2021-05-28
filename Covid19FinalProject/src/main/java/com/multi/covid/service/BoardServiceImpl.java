package com.multi.covid.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardMapper boardMapper;

	@Override
	public String getRecentLive(String location) {
		JsonObject jsonObject = new JsonObject();
		LiveVO sqlResult = boardMapper.getRecentLive();
		int count = 0;

		if (location.equals("전체")) {
			sqlResult.calSum();
			count = sqlResult.getSum();
		}
		else if (location.equals("서울")) count = sqlResult.getSeoul();
		else if (location.equals("부산")) count = sqlResult.getBusan();
		else if (location.equals("인천")) count = sqlResult.getIncheon();
		else if (location.equals("대구")) count = sqlResult.getDaegu();
		else if (location.equals("광주")) count = sqlResult.getGwangju();
		else if (location.equals("대전")) count = sqlResult.getDaejeon();
		else if (location.equals("울산")) count = sqlResult.getUlsan();
		else if (location.equals("세종")) count = sqlResult.getSejong();
		else if (location.equals("경기")) count = sqlResult.getGyeonggi();
		else if (location.equals("강원")) count = sqlResult.getGangwon();
		else if (location.equals("충북")) count = sqlResult.getChungbuk();
		else if (location.equals("충남")) count = sqlResult.getChungnam();
		else if (location.equals("경북")) count = sqlResult.getGyeongbuk();
		else if (location.equals("경남")) count = sqlResult.getGyeongnam();
		else if (location.equals("전북")) count = sqlResult.getJeonbuk();
		else if (location.equals("전남")) count = sqlResult.getJeonnam();
		else if (location.equals("제주")) count = sqlResult.getJeju();
	
		jsonObject.addProperty("live_count", count);
		
		return jsonObject.toString();
	}

	@Override
	public String getRecentTwoResult(String location) {
		JsonObject jsonObject = new JsonObject();
		
		if (location.equals("전체")) location = "합계";
		
		List<ResultVO> sqlResult = boardMapper.getRecentTwoResult(location);
		
		jsonObject.addProperty("total_count", sqlResult.get(0).getTotal_count());
		jsonObject.addProperty("increment_count", sqlResult.get(0).getIncrement_count());
		jsonObject.addProperty("clear_count", sqlResult.get(0).getClear_count());
		jsonObject.addProperty("compare_clear_count", sqlResult.get(0).getClear_count() - sqlResult.get(1).getClear_count());
		jsonObject.addProperty("death_count", sqlResult.get(0).getDeath_count());
		jsonObject.addProperty("compare_death_count", sqlResult.get(0).getDeath_count() - sqlResult.get(1).getDeath_count());
		
		return jsonObject.toString();
	}
}
