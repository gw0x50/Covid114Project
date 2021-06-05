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
	List<ResultVO> getOneResult(String date);		
	LiveVO getOneLive(String date);	
	List<CenterVO> getAllCenter();
}
