package com.multi.covid.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

@Mapper
@Repository
public interface BoardMapper {
	LiveVO getRecentLive(); // 최근 실시간 확진자 정보 조회

	List<ResultVO> getRecentTwoResult(String location); // 최근 이틀간 확진자 정보 조회
}