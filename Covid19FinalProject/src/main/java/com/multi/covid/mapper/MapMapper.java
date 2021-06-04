package com.multi.covid.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.multi.covid.domain.CenterVO;

@Mapper
@Repository
public interface MapMapper {
	List<CenterVO> getAllCenter(Double lat,Double lng);
}
