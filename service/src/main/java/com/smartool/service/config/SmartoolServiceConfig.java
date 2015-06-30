package com.smartool.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan(basePackages = {"com.smartool.service.controller"})
public class SmartoolServiceConfig extends WebMvcConfigurationSupport{
	
}
