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
public interface ChatbotMapper {
	ResultVO getOneResult(String location);
	int getLocLive(HashMap<String, String> map);
	LiveVO getOneLive(String date);
	List<CenterVO> getAllCenter();
	List<CenterVO> getLocCenter(String location);
	List<String> getAddrTwo(String location);
	List<String> getAddrThree(String location);
	List<CenterVO> getAddrCenter(String address);
	List<CenterVO> getAddrCenter_2(String facility_name);
}
