package com.cl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;

@SuppressWarnings("unchecked")

public class FeignOrderService {
	
	/*
	 *  rest + ribbon实现负载均衡的方式
	 * 
	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "orderError")
	public List<String> getUserToMember(){
		
		 return restTemplate.getForObject("localhost:service-member/getUserList", List.class);
	}
	
	public List<String> orderError(){
		List<String> list = new ArrayList<>();
		list.add("服务发生异常了哦。。。。。。");
		return list;
	}
*/
	
}
