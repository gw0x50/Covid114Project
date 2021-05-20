package com.multi.covid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multi.covid.domain.LiveVO;
import com.multi.covid.mapper.LiveMapper;

@Service
public class LiveServiceImpl implements LiveService{
	@Autowired
	private LiveMapper liveMapper;
	
	public void setLiveMapper(LiveMapper liveMapper) {
		this.liveMapper = liveMapper;
	}
	
	@Override
	public LiveVO getOneLive(String date) {
		return liveMapper.getOneLive(date);
	}
}
