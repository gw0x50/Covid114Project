package com.multi.covid.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

@Mapper
@Repository
public interface ChartMapper {
	ResultVO getOneResult(String date);
	ArrayList<ResultVO> getBetweenResult(HashMap<String, String> map);
	List<ResultVO> getAllResult();
	LiveVO getOneLive(String date);
	List<CenterVO> getAllCenter();
}
