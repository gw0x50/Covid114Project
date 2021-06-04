package com.multi.covid.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.ChartMapper;

@Service
public class ChartServiceImpl implements ChartService {
	@Autowired
	private ChartMapper mapper;

	// 최근 7일간 확진자 정보 조회
	@Override
	public String get7DaysResult(String location) {
		Gson gson = new Gson();

		List<ResultVO> sqlResult = mapper.get7DaysResult(location); // sql 호출 및 응답 정보 저장

		// 응답 정보 내부 데이터 변경
		for (int i = 0; i < 7; i++) {
			// 각 데이터의 result_date(데이터 입력일) 정보 가공
			// ex) 2021-06-04 -> 06/04
			sqlResult.get(i).setResult_date(sqlResult.get(i).getResult_date().substring(5).replaceAll("-", "/"));
		}

		return gson.toJson(sqlResult);

	}

	// 최근 4주간 확진자 정보 조회
	@Override
	public String get4WeeksResult(String location) {
		Gson gson = new Gson();

		List<ResultVO> sqlResult = mapper.get4WeeksResult(location); // sql 호출 및 응답 정보 저장

		List<ResultVO> returnList = new ArrayList<ResultVO>();

		// 28개의 데이터를 한 주 단위로 묶어서 총 4주의 데이터로 가공
		// sqlResult의 맨 앞에 있는 값을 사용 후 remove하여 다음 값 호출
		// 한 주 단위 반복문
		for (int i = 0; i < 4; i++) {
			ResultVO temp = new ResultVO();

			// 한 주의 시작 부분을 기준으로 날짜와 지역 정보 저장
			temp.setResult_date(sqlResult.get(0).getResult_date().substring(5).replaceAll("-", "/")); // ex) 2021-06-04 -> 06/04
			temp.setLocation(sqlResult.get(0).getLocation());

			// 하루 단위 반복문
			for (int j = 0; j < 7; j++) {
				temp.setIncrement_count(temp.getIncrement_count() + sqlResult.get(0).getIncrement_count()); // 일일 증가 확진자 수 합산
				sqlResult.remove(0); // list 맨 앞의 데이터 삭제 = 다음 데이터 호출 준비
			}
			returnList.add(temp);
		}
		return gson.toJson(returnList);
	}

	// 최근 12개월간 확진자 정보 조회
	@Override
	public String get12MonthsResult(String location) {
		Gson gson = new Gson();
		
		List<ResultVO> sqlResult = mapper.get12MonthsResult(location); // sql 호출 및 응답 정보 저장

		List<ResultVO> returnList = new ArrayList<ResultVO>();
		
		// 12개월 단위 반복문
		for (int i = 0; i < 12; i++) {
			ResultVO temp = new ResultVO();

			temp = sqlResult.get(i);
			temp.setResult_date(temp.getResult_date().substring(5, 7) + "월"); // ex) 2021-06 -> 06월
			returnList.add(temp);
		}

		return gson.toJson(returnList);
	}
}
