package com.multi.covid.service;

import com.multi.covid.domain.LiveVO;

public interface LiveService{
	public LiveVO getOneLive(String date);
}
