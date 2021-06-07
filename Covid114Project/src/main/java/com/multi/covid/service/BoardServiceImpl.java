package com.multi.covid.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardMapper mapper;

	// 최근 실시간 확진자 정보 조회
	@Override
	public String getRecentLive(String location) {
		JsonObject jsonObject = new JsonObject();

		LiveVO sqlResult = mapper.getRecentLive(); // sql 호출 및 응답 정보 저장

		int sum = 0; // 실시간 확진자 수

		// 원하는 지역의 확진자 수를 sum에 저장
		if (location.equals("전체")) {
			sqlResult.calSum(); // 전 지역 확진자 수의 경우는 calSum()을 통한 연산 후 sum 호출
			sum = sqlResult.getSum();
		}
		else if (location.equals("서울")) sum = sqlResult.getSeoul();
		else if (location.equals("부산")) sum = sqlResult.getBusan();
		else if (location.equals("인천")) sum = sqlResult.getIncheon();
		else if (location.equals("대구")) sum = sqlResult.getDaegu();
		else if (location.equals("광주")) sum = sqlResult.getGwangju();
		else if (location.equals("대전")) sum = sqlResult.getDaejeon();
		else if (location.equals("울산")) sum = sqlResult.getUlsan();
		else if (location.equals("세종")) sum = sqlResult.getSejong();
		else if (location.equals("경기")) sum = sqlResult.getGyeonggi();
		else if (location.equals("강원")) sum = sqlResult.getGangwon();
		else if (location.equals("충북")) sum = sqlResult.getChungbuk();
		else if (location.equals("충남")) sum = sqlResult.getChungnam();
		else if (location.equals("경북")) sum = sqlResult.getGyeongbuk();
		else if (location.equals("경남")) sum = sqlResult.getGyeongnam();
		else if (location.equals("전북")) sum = sqlResult.getJeonbuk();
		else if (location.equals("전남")) sum = sqlResult.getJeonnam();
		else if (location.equals("제주")) sum = sqlResult.getJeju();

		jsonObject.addProperty("live_count", sum);

		return jsonObject.toString();
	}

	// 최근 이틀간 확진자 정보 조회
	@Override
	public String getRecentTwoResult(String location) {
		JsonObject jsonObject = new JsonObject();

		if (location.equals("전체")) location = "합계"; // DB는 합계라는 명칭을 사용하므로 명칭 변환

		List<ResultVO> sqlResult = mapper.getRecentTwoResult(location); // sql 호출 및 응답 정보 저장

		jsonObject.addProperty("total_count", sqlResult.get(0).getTotal_count()); // 누적 확진자 수
		jsonObject.addProperty("increment_count", sqlResult.get(0).getIncrement_count()); // 전일 대비 증가한 확진자 수
		jsonObject.addProperty("clear_count", sqlResult.get(0).getClear_count()); // 누적 완치자 수
		jsonObject.addProperty("compare_clear_count", sqlResult.get(0).getClear_count() - sqlResult.get(1).getClear_count()); // 전일 대비 증가한 완치자 수
		jsonObject.addProperty("death_count", sqlResult.get(0).getDeath_count()); // 누적 사망자 수
		jsonObject.addProperty("compare_death_count", sqlResult.get(0).getDeath_count() - sqlResult.get(1).getDeath_count()); // 전일 대비 증가한 사망자 수

		return jsonObject.toString();
	}
}