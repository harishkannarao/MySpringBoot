package com.harishkannarao.jdbc;

import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

	@Autowired
	@Value("${app.uploads-dir}")
	private String uploadsDir;

	@Test
	public void test_file_upload_and_download() throws IOException {
		String fileName1 = "form_file_upload_1.txt";
		String fileName2 = "form_file_upload_2.txt";

		Path existingUploadedFile1 = Paths.get(uploadsDir).resolve(fileName1);
		if (Files.exists(existingUploadedFile1)) {
			Files.delete(existingUploadedFile1);
		}

		Path existingUploadedFile2 = Paths.get(uploadsDir).resolve(fileName2);
		if (Files.exists(existingUploadedFile2)) {
			Files.delete(existingUploadedFile2);
		}

		Path file1 = Paths.get(new ClassPathResource(fileName1).getFile().getAbsolutePath());
		Path file2 = Paths.get(new ClassPathResource(fileName2).getFile().getAbsolutePath());
		String file1Content = Files.readString(file1);
		assertThat(file1Content).contains(
			"Sample file to verify form upload",
			"Also to verify download of file");
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		parts.add("firstName", "first");
		parts.add("lastName", "last");
		parts.add("files", new FileSystemResource(file1));
		parts.add("files", new FileSystemResource(file2));

		ResponseEntity<Void> uploadResult = nonBufferingRestClient.post()
			.uri(formUploadEndpointUrl)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.body(parts)
			.retrieve()
			.toBodilessEntity();

		assertThat(uploadResult.getStatusCode().value()).isEqualTo(302);

		Path downloadedFile = Files.createTempDirectory("test" + UUID.randomUUID()).resolve("downloaded_form_file_upload_1.txt");
		nonBufferingRestClient.method(HttpMethod.GET)
			.uri(fileDownloadEndpointUrl + "/{fileName}", Map.of("fileName", fileName1))
			.exchange((clientRequest, clientResponse) -> {
				try (
					BufferedInputStream inputStream = new BufferedInputStream(clientResponse.getBody(), 16 * 1024);
					BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(downloadedFile, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND), 16 * 1024)
				) {
					inputStream.transferTo(outputStream);
				}
				return null;
			});

		String downloadedFile1Content = Files.readString(downloadedFile);

		assertThat(downloadedFile1Content)
			.isEqualTo(file1Content);
	}

	@Test
	public void test_file_upload_download_using_streaming_apache_http_client() throws IOException {
		String fileName1 = "form_file_upload_1.txt";
		String fileName2 = "form_file_upload_2.txt";

		Path existingUploadedFile1 = Paths.get(uploadsDir).resolve(fileName1);
		if (Files.exists(existingUploadedFile1)) {
			Files.delete(existingUploadedFile1);
		}

		Path existingUploadedFile2 = Paths.get(uploadsDir).resolve(fileName2);
		if (Files.exists(existingUploadedFile2)) {
			Files.delete(existingUploadedFile2);
		}

		Path file1 = Paths.get(new ClassPathResource(fileName1).getFile().getAbsolutePath());
		Path file2 = Paths.get(new ClassPathResource(fileName2).getFile().getAbsolutePath());
		String file1Content = Files.readString(file1);

		try (
			BufferedInputStream file1BufStream = new BufferedInputStream(Files.newInputStream(file1), 16 * 1024);
			BufferedInputStream file2BufStream = new BufferedInputStream(Files.newInputStream(file2), 16 * 1024);
		) {
			final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("firstName", "first");
			builder.addTextBody("lastName", "last");
			builder.addBinaryBody("files", file1BufStream, ContentType.TEXT_PLAIN, fileName1);
			builder.addBinaryBody("files", file2BufStream, ContentType.TEXT_PLAIN, fileName2);
			try (HttpEntity entity = builder.build()) {
				Response response = Request.post(formUploadEndpointUrl)
					.body(entity)
					.execute();

				assertThat(response.returnResponse().getCode()).isEqualTo(302);
			}
		}

		Path downloadedFile = Files.createTempDirectory("test" + UUID.randomUUID()).resolve("downloaded_form_file_upload_1.txt");
		String url = UriComponentsBuilder.fromUri(fileDownloadEndpointUrl)
			.pathSegment(fileName1)
			.toUriString();
		Request.get(url).execute().handleResponse(response -> {
			assertThat(response.getCode()).isEqualTo(200);
			try (
				BufferedInputStream bufferedInputStream = new BufferedInputStream(response.getEntity().getContent(), 16 * 1024);
				BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(downloadedFile, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND), 16 * 1024)
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
