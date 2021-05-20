package com.multi.covid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.multi.covid.controller.AISpeakerController;
import com.multi.covid.controller.ChatBotController;
import com.multi.covid.controller.WebController;
import com.multi.covid.mapper.CenterMapper;
import com.multi.covid.mapper.LiveMapper;
import com.multi.covid.mapper.ResultMapper;

@ComponentScan(basePackages = "com.multi.covid.controller")
@ComponentScan(basePackages = "com.multi.covid.service")
@MapperScan(basePackages = "com.multi.covid.mapper")
@SpringBootApplication
public class Covid19FinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Covid19FinalProjectApplication.class, args);
	}

}
