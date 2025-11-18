package com.harishkannarao.jdbc.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Objects.requireNonNull;

@RestController
public class SampleFormRestController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Path uploadsPath;

	@Autowired
	public SampleFormRestController(
		@Value("${app.uploads-dir}") String uploadsDir) {
		this.uploadsPath = Paths.get(uploadsDir);
	}

	@PostMapping(path = "/form-submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> handleForm(
		@RequestPart(value = "firstName", required = false) String firstName,
		@RequestPart(value = "lastName", required = false) String lastName,
		@RequestPart(value = "files", required = false) List<MultipartFile> files)
		throws IOException {
		logger.info("Upload Location {}", uploadsPath.toAbsolutePath());
		logger.info("Text fields {}, {}", firstName, lastName);
		for (MultipartFile file : files) {
			logger.info("Uploading file {}", file.getOriginalFilename());
			Path targetPath = uploadsPath.resolve(requireNonNull(file.getOriginalFilename()));
			Files.createDirectories(targetPath.getParent());
			// buffer 16 bytes from input and write it to output, buffered input and output stream allows to control buffer size
			try (
					 BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream(), 16 * 1024);
					 BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(targetPath), 16 * 1024)
			) {
				inputStream.transferTo(outputStream);
			}
		}
		return ResponseEntity.status(HttpStatus.FOUND)
			.header(HttpHeaders.LOCATION, "/sample_form.html")
			.build();
	}

	@GetMapping("/files/{name}")
	public void getArchive(@PathVariable("name") String name, HttpServletResponse response) throws IOException {
		logger.info("Downloading file {}", name);
		Path file = uploadsPath.resolve(name);

		if (!Files.exists(file)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		// buffer 16 bytes from input and write it to output, buffered input and output stream allows to control buffer size
		try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(file), 16 * 1024);
				 BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream(), 16 * 1024)) {
			response.setStatus(HttpStatus.OK.value());
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(name));
			response.setContentLengthLong(Files.size(file));
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			inputStream.transferTo(outputStream);
		}
	}
}
