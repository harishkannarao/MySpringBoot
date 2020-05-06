package com.harishkannarao.jdbc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HelloPageController {

	@GetMapping
	public String displayHomePage(
			@RequestParam("displayDate") Optional<String> displayDateParam,
			HttpServletRequest request,
			Map<String, Object> model
	) {
		Boolean displayDate = displayDateParam.map(Boolean::parseBoolean).orElse(false);
		if (displayDate) {
			model.put("utc_date", OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC));
		}
		return "root/home_page";
	}

}
