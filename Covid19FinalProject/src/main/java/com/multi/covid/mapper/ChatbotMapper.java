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
	ResultVO getResult(String location); // 누적 확진자 수 조회 
	LiveVO getLive(String date); // 실시간 확진자 수 조회 
	int getLocLive(HashMap<String, String> map); // 실시간 지역 확진자 수 조회 
	List<CenterVO> getLocCenter(String location); // 지역별 센터 조회 
	List<String> getAddrTwo(String location); // 주소 두 번째 값 조회
	List<String> getAddrThree(String address_two); //주소 세 번째 값 조회 
	List<String> getAddrFour(String address_three); //주소 네 번째 값 조회 
	List<CenterVO> getAddrCenter(String address_three); // 주소로 백신 센터 조회
	List<CenterVO> getFacility(String facility_name); // 시설 이름으로 백신 센터 조회
	List<CenterVO> getFacility_loc(HashMap<String, String> map); //시설, 지역 이름으로 백신 센터 조회 
}
