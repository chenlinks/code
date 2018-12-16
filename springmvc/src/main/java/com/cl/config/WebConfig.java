package com.cl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * springmvc的配置信息
 * @author chenl
 *
 */
@Configuration  //等同于配置文件上的bean
@EnableWebMvc   //开启springmvc的功能
@ComponentScan(basePackages="com.cl.controller")  //扫描包
public class WebConfig extends WebMvcConfigurerAdapter{
	
	//配置视图转换器
	// 创建SpringMVC视图解析器
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
        //可以在JSP页面中通过${}访问beans
		viewResolver.setExposeContextBeansAsAttributes(true);
		return viewResolver;
	}

	
}
