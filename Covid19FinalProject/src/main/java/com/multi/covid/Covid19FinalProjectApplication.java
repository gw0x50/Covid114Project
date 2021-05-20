package com.multi.covid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.multi.covid.controller")
@ComponentScan(basePackages = "com.multi.covid.service")
@MapperScan(basePackages = "com.multi.covid.mapper")
@SpringBootApplication
public class Covid19FinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Covid19FinalProjectApplication.class, args);
	}

}
