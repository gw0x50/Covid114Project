package com.multi.covid.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.mapper.CenterMapper;

@Service
public class CenterServiceImpl implements CenterService{
	@Autowired
	private CenterMapper centerMapper;
	
	public void setCenterMapper(CenterMapper cetnerMapper) {
		this.centerMapper = cetnerMapper;
	}
	
	@Override
	public List<CenterVO> getAllCenter() {
		return centerMapper.getAllCenter();
	}	
}
