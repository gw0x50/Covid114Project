package com.multi.covid.service;

import java.util.List;

import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

public interface ChartService {
	public List<ResultVO> get7DaysResult(String location);
	public List<ResultVO> get4WeeksResult(String location);
	public List<ResultVO> get12MonthsResult(String location);
	LiveVO getOneLive(String date);
}
