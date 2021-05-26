package com.multi.covid.mapper;

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
	List<ResultVO> get7DaysResult(String location);
	List<ResultVO> get4WeeksResult(String location);
	List<ResultVO> get12MonthsResult(String location);
	List<ResultVO> getAllResult();
	LiveVO getOneLive(String date);
	List<CenterVO> getAllCenter();
}
