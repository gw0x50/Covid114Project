package com.multi.covid.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

@Mapper
@Repository
public interface MapMapper {
	ResultVO getOneResult(String date);
	List<ResultVO> getBetweenResult(HashMap<String, String> map);
	List<ResultVO> getAllResult();
	LiveVO getOneLive(String date);
	List<CenterVO> getAllCenter(Double lat,Double lng);
	List<CenterVO> getAllCentertemp(Double lat,Double lng);
}
