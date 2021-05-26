package com.multi.covid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multi.covid.domain.LiveVO;
import com.multi.covid.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardMapper boardMapper;

	@Override
	public LiveVO getOneLive(String date) {
		LiveVO sqlResult = boardMapper.getOneLive(date);
		return sqlResult;
	}
}
