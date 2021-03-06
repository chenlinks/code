package com.cl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableEurekaClient//启用eureka客户端
@EnableFeignClients//启用feign客户端
@EnableHystrix //启用Hystrix断路器
public class HystrixApp {
	
	public static void main(String[] args) {
		SpringApplication.run(HystrixApp.class, args);
	}
}
