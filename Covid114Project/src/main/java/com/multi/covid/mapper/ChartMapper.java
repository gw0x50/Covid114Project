package com.multi.covid.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.multi.covid.domain.ResultVO;

@Mapper
@Repository
public interface ChartMapper {
	List<ResultVO> get7DaysResult(String location); // 최근 7일간 확진자 정보 조회

	List<ResultVO> get4WeeksResult(String location); // 최근 4달간 확진자 정보 조회

	List<ResultVO> get12MonthsResult(String location); // 최근 12개월간 확진자 정보 조회
}