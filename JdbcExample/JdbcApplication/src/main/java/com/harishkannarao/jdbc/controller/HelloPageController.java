package com.harishkannarao.jdbc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HelloPageController {

	@GetMapping
	public ModelAndView displayHomePage(
			@RequestParam("displayDate")
			String displayDateParam,
			HttpServletRequest request
	) {
		Boolean displayDate = Optional.ofNullable(displayDateParam).map(Boolean::parseBoolean).orElse(false);
		ModelAndView modelAndView = new ModelAndView("root/home_page");
		if (displayDate) {
			modelAndView.addObject("utc_date", OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC));
		}
		return modelAndView;
	}
}
