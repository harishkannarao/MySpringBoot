package com.harishkannarao.war;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity redirectToIndexPage(HttpEntity requestEntity) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", "app/index.html");
		return new ResponseEntity(responseHeaders, HttpStatus.SEE_OTHER);
	}

}
