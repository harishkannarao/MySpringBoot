package com.harishkannarao.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;

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

		Path downloadedFile = Files.createTempDirectory("test" + UUID.randomUUID()).resolve("downloaded_form_file_upload_1.txt");
		restClient.method(HttpMethod.GET)
			.uri(fileDownloadEndpointUrl + "/{fileName}", Map.of("fileName", "form_file_upload_1.txt"))
			.exchange((clientRequest, clientResponse) -> {
				try (
					InputStream resStream = clientResponse.getBody();
					BufferedInputStream bufferedInputStream = new BufferedInputStream(resStream, 16 * 1024);
					OutputStream outputStream = Files.newOutputStream(downloadedFile, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND)
				) {
					bufferedInputStream.transferTo(outputStream);
				}
				return null;
			});

		String downloadedFile1Content = Files.readString(downloadedFile);

		assertThat(downloadedFile1Content)
			.isEqualTo(file1Content);
	}
}
