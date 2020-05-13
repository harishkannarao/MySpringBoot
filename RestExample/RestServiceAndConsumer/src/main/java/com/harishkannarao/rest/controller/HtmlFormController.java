package com.harishkannarao.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collections;

@Controller
@RequestMapping("/form")
public class HtmlFormController {

	@GetMapping
	public ModelAndView displayForm() {
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
		formData.set("name", "Enter your name");
		ModelAndView modelAndView = new ModelAndView("form/form_template");
		modelAndView.addObject("formData", formData);
		return modelAndView;
	}

	@PostMapping
	public ModelAndView submitForm(@RequestBody MultiValueMap<String, Object> formData) {
		ModelAndView modelAndView;
		if (formData != null && formData.containsKey("favoritePet") && formData.get("favoritePet").size() > 1) {
			modelAndView = new ModelAndView(new RedirectView("/form/success"));
		} else {
			modelAndView = new ModelAndView("form/form_template");
			modelAndView.addObject("formData", formData);
			modelAndView.addObject("errorMessage", "Please select at least 2 pets");
		}
		return modelAndView;
	}

	@GetMapping("success")
	public ModelAndView displayFormSuccess() {
		return new ModelAndView("form/form_success_template");
	}

}
