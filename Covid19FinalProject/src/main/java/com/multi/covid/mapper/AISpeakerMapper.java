package com.multi.covid.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

@Mapper
@Repository
public interface AISpeakerMapper {
	List<ResultVO> getOneResult(String date); // 특정 날짜 누적 확진자 수 조회

	LiveVO getOneLive(String date); // 실시간 확진자 수 조회

	List<CenterVO> getAllCenter(); // 백신센터 리스트 조회
}
