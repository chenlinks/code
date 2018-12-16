package com.cl.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MumberServiceController {
	
	
	@Value("${server.port}")
	private String servicePort;
	
	@RequestMapping("/getUserList")
	@ResponseBody
	public List<String>  getUserList(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArrayList<String> list = new ArrayList<>();
		list.add("chenling");
		list.add("yaoxia");
		list.add("caicia");
		list.add("erya");
		list.add("服务端口号："+servicePort);
		return list;
	}
}
