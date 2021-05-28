package com.multi.covid.service;

import java.util.List;

import com.multi.covid.domain.LiveVO;

public interface BoardService {
	String getRecentLive(String location);
	String getRecentTwoResult(String location);
}
