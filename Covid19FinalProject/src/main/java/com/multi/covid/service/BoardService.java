package com.multi.covid.service;

import com.multi.covid.domain.LiveVO;

public interface BoardService {
	LiveVO getOneLive(String date);
}
