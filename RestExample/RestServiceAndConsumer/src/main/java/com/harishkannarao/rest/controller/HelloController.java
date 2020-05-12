package com.harishkannarao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class HelloController {
	private final String message;

	@Autowired
	public HelloController(@Value("${application.message:Hello World}") String message) {
		this.message = message;
	}

	@RequestMapping("/hello")
	public ModelAndView sayHello() {
		ModelAndView modelAndView = new ModelAndView("hello");
		modelAndView.addObject("date", LocalDate.now());
		modelAndView.addObject("message", this.message);
		return modelAndView;
	}

}
