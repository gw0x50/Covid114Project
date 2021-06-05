package com.multi.covid.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.multi.covid.domain.CenterVO;

@Mapper
@Repository
public interface MapMapper {
	List<CenterVO> getLocalCenter(Double lat,Double lng); //위도,경도 주변 백신 접종 센터 정보 조회
}
