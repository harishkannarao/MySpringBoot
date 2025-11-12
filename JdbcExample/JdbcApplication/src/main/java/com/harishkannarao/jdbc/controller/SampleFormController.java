package com.harishkannarao.jdbc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
public class SampleFormController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping(path = "/form-submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public RedirectView handleForm(
		@RequestPart(value = "firstName", required = false) String firstName,
		@RequestPart(value = "lastName", required = false) String lastName,
		@RequestPart(value = "files", required = false) List<MultipartFile> files) {
		List<String> fileNames = Objects.<List<MultipartFile>>requireNonNullElse(
				files,
				Collections.emptyList())
			.stream()
			.map(MultipartFile::getOriginalFilename)
			.toList();
		logger.info("{}, {}, {}", firstName, lastName, fileNames);
		return new RedirectView("/sample_form.html");
	}
}
