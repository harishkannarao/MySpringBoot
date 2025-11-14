package com.harishkannarao.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class SampleFormRestControllerIT extends BaseIntegrationJdbc {
	@Autowired
	@Value("${formUploadEndpointUrl}")
	private URI formUploadEndpointUrl;

	@Autowired
	@Value("${fileDownloadEndpointUrl}")
	private URI fileDownloadEndpointUrl;

	@Test
	public void test_file_upload_and_download() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("form_file_upload_test.txt");
		Path path = Paths.get(classPathResource.getFile().getAbsolutePath());
		System.out.println("path.toAbsolutePath() = " + path.toAbsolutePath());
		String content = Files.readString(path);
		assertThat(content).contains(
			"Sample file to verify form upload",
			"Also to verify download of file");
	}
}
