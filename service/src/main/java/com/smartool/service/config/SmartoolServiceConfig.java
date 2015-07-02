package com.smartool.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.smartool.service.dao.MockedUserDaoImpl;
import com.smartool.service.dao.UserDao;

@Configuration
@ComponentScan(basePackages = { "com.smartool.service.*" })
public class SmartoolServiceConfig extends WebMvcConfigurationSupport {
	@Bean
	public UserDao getUserDao() {
		return new MockedUserDaoImpl();
	}
}
