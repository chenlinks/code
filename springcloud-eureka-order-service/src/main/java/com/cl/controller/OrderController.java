package com.cl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cl.service.MumberService;


@Controller
public class OrderController {
	
	@Autowired
	private MumberService mumberService;
	
	@RequestMapping("/getAllUserOrder")
	@ResponseBody
	public List<String> getAllUserOrder(){
		System.out.println("*******************调用会员服务******************************");
		return mumberService.getOrderByUserList();
	}
}
