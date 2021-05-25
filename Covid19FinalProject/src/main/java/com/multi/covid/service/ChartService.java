package com.multi.covid.service;

import java.util.ArrayList;
import java.util.List;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

public interface ChartService {
	LiveVO getOneLive(String date);
	List<CenterVO> getAllCenter();
	ResultVO getOneResult(String date);
	List<ResultVO[]> getBetweenResult(String startDate, String endDate);
	List<ResultVO[]> getAllResult();
	public ArrayList<ArrayList<ResultVO>> get7DaysResult();
	public ArrayList<ArrayList<ResultVO>> get4WeeksResult();
	public ArrayList<ArrayList<ResultVO>> get12MonthsResult();
}
