package com.multi.covid.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.ChartMapper;

@Service
public class ChartServiceImpl implements ChartService {
	@Autowired
	private ChartMapper chartMapper;

	@Override
	public ResultVO getOneResult(String date) {
		return chartMapper.getOneResult(date);
	}

	@Override
	public List<ResultVO[]> getBetweenResult(String startDate, String endDate) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		System.out.println(chartMapper.getBetweenResult(map));
		return null;
	}

	@Override
	public List<ResultVO[]> getAllResult() {
		List<ResultVO> list = chartMapper.getAllResult();
		System.out.println(list);
		return null;
	}

	@Override
	public LiveVO getOneLive(String date) {
		return chartMapper.getOneLive(date);
	}

	@Override
	public List<CenterVO> getAllCenter() {
		return chartMapper.getAllCenter();
	}

	public ArrayList<ArrayList<ResultVO>> get7DaysResult() {
		Calendar today = Calendar.getInstance();
		Calendar daysBefore = Calendar.getInstance();
		
		if(today.get(Calendar.HOUR)<10) { // 00:00 ~ 09:59이면 데이터 갱신이 안되어있으므로 
			today.add(Calendar.DATE, -2);
			daysBefore.add(Calendar.DATE, -8);
		}
		else {
			today.add(Calendar.DATE, -1);
			daysBefore.add(Calendar.DATE, -7);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String strYesterday = sdf.format(today.getTime());
		String strDaysBefore = sdf.format(daysBefore.getTime());
		
		System.out.println(strYesterday);
		System.out.println(strDaysBefore);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("startDate", strDaysBefore);
		map.put("endDate", strYesterday);
		ArrayList<ResultVO> sqlResult = chartMapper.getBetweenResult(map);
		
		System.out.println(sqlResult);
		System.out.println(sqlResult.size());
		
		ArrayList<ArrayList<ResultVO>> returnList = new ArrayList<ArrayList<ResultVO>>();
		// 합계, 충북, 충남, 제주, 전북, 전남, 인천, 울산, 세종, 서울,
		// 부산, 대전, 대구, 광주, 경북, 경남, 경기, 검역, 강원 (총 19지역)
		for(int i = 0; i < 19; i++) {
			ArrayList<ResultVO> temp = new ArrayList<ResultVO>();
			for(int j = 0; j < 7; j++) {
				temp.add(sqlResult.remove(0));
			}
			returnList.add(temp);
		}
		System.out.println();
		return returnList;
	}
}
