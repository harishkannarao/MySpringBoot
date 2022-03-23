package com.harishkannarao.jdbc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/task-detail")
public class TaskPageController {

	@GetMapping()
	public ModelAndView displayTaskDetail(@RequestParam(value = "id") String id) {
		ModelAndView modelAndView = new ModelAndView("task/task_detail");
		modelAndView.addObject("id", id);
		return modelAndView;
	}
}
