package com.cl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
	
	
	@RequestMapping(value="/pageIndex" )
	public String pageIndex() {
		return "pageIndex";
	}
}
