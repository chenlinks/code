package com.cl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer//����eurekaservice����
public class EurekaServiceApp02 {

		public static void main(String[] args) {
			SpringApplication.run(EurekaServiceApp02.class, args);
		}
}
