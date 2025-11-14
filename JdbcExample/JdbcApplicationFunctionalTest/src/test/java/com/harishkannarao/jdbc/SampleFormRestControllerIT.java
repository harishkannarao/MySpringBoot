package com.harishkannarao.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
		Path file1 = Paths.get(new ClassPathResource("form_file_upload_1.txt").getFile().getAbsolutePath());
		Path file2 = Paths.get(new ClassPathResource("form_file_upload_2.txt").getFile().getAbsolutePath());
		String file1Content = Files.readString(file1);
		assertThat(file1Content).contains(
			"Sample file to verify form upload",
			"Also to verify download of file");
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		parts.add("firstName", "first");
		parts.add("lastName", "last");
		parts.add("files", new FileSystemResource(file1));
		parts.add("files", new FileSystemResource(file2));

		ResponseEntity<Void> uploadResult = restClient.post()
			.uri(formUploadEndpointUrl)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.body(parts)
			.retrieve()
			.toBodilessEntity();

		assertThat(uploadResult.getStatusCode().value()).isEqualTo(302);
	}
}
