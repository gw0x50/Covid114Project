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
	private ChartMapper chartMapper;

	@Override
	public String get7DaysResult(String location) {
		Gson gson = new Gson();
		List<ResultVO> sqlResult = chartMapper.get7DaysResult(location);

		for (int i = 0; i < 7; i++) {
			sqlResult.get(i).setResult_date(sqlResult.get(i).getResult_date().substring(5).replaceAll("-", "/"));
		}

		return gson.toJson(sqlResult);

	}

	@Override
	public String get4WeeksResult(String location) {
		Gson gson = new Gson();
		List<ResultVO> sqlResult = chartMapper.get4WeeksResult(location);

		List<ResultVO> returnList = new ArrayList<ResultVO>();
		for (int i = 0; i < 4; i++) {
			ResultVO temp = new ResultVO();
			temp.setResult_date(sqlResult.get(0).getResult_date().substring(5).replaceAll("-", "/"));
			temp.setLocation(sqlResult.get(0).getLocation());
			for (int j = 0; j < 7; j++) {
				temp.setIncrement_count(temp.getIncrement_count() + sqlResult.get(0).getIncrement_count());
				if (j == 6) {
					// temp.setResult_date(temp.getResult_date() + " ~ " + sqlResult.get(0).getResult_date().substring(5));
					temp.setTotal_count(sqlResult.get(0).getTotal_count());
				}
				sqlResult.remove(0);
			}
			returnList.add(temp);
		}
		return gson.toJson(returnList);
	}

	@Override
	public String get12MonthsResult(String location) {
		Gson gson = new Gson();
		List<ResultVO> sqlResult = chartMapper.get12MonthsResult(location);

		List<ResultVO> returnList = new ArrayList<ResultVO>();
		for (int i = 0; i < 12; i++) {
			ResultVO temp = new ResultVO();

			temp = sqlResult.get(i);
			temp.setResult_date(temp.getResult_date().substring(5, 7) + "월");
			returnList.add(temp);
		}

		return gson.toJson(returnList);
	}
}
