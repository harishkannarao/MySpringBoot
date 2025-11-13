package com.harishkannarao.jdbc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@RestController
public class SampleFormController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Path uploadsPath;

	@Autowired
	public SampleFormController(
		@Value("${app.uploads-dir}") String uploadsDir) {
		this.uploadsPath = Paths.get(uploadsDir);
	}

	@PostMapping(path = "/form-submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> handleForm(
		@RequestPart(value = "firstName", required = false) String firstName,
		@RequestPart(value = "lastName", required = false) String lastName,
		@RequestPart(value = "files", required = false) List<MultipartFile> files)
		throws IOException {
		List<String> fileNames = Objects.<List<MultipartFile>>requireNonNullElse(
				files,
				Collections.emptyList())
			.stream()
			.map(multipartFile -> Objects.requireNonNull(multipartFile.getOriginalFilename()))
			.sorted()
			.toList();
		logger.info("{}", uploadsPath.toAbsolutePath());
		logger.info("{}, {}, {}", firstName, lastName, fileNames);

		for (MultipartFile file : files) {
			Path targetPath = uploadsPath.resolve(requireNonNull(file.getOriginalFilename()));
			Files.createDirectories(targetPath.getParent());
			try (InputStream inputStream = file.getInputStream();
					 // buffer 16 bytes from input and write it to output, buffered input stream allows to control buffer size
					 BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 16 * 1024);
					 OutputStream outputStream = Files.newOutputStream(targetPath)) {
				bufferedInputStream.transferTo(outputStream);
			}
		}
		return ResponseEntity.status(HttpStatus.FOUND)
			.header(HttpHeaders.LOCATION, "/sample_form.html")
			.build();
	}
}
