package com.multi.covid.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;
import com.multi.covid.mapper.ChatbotMapper;

@Service
public class ChatbotServiceImpl implements ChatbotService{
	@Autowired
	private ChatbotMapper chatbotMapper;

	@Override
	public ResultVO getOneResult(String date) {
		return chatbotMapper.getOneResult(date);
	}

	@Override
	public List<ResultVO> getBetweenResult(HashMap<String, String> map) {
		return chatbotMapper.getBetweenResult(map);
	}

	@Override
	public List<ResultVO> getAllResult() {
		return chatbotMapper.getAllResult();
	}

	@Override
	public LiveVO getOneLive(String date) {
		return chatbotMapper.getOneLive(date);
	}

	@Override
	public List<CenterVO> getAllCenter() {
		return chatbotMapper.getAllCenter();
	}
}
