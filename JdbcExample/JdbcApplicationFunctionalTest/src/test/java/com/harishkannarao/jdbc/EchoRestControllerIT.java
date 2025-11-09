package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.domain.DeeplyImmutableRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EchoRestControllerIT extends BaseIntegrationJdbc {
	@Autowired
	@Value("${echoEndpointUrl}")
	private URI echoEndpointUrl;

	@Test
	public void test_echo_with_all_fields() {
		final DeeplyImmutableRecord input = new DeeplyImmutableRecord(
			2,
			"text-value",
			Optional.of("optional-text-value"),
			List.of("value1", "value2"),
			Set.of("set-value-1", "set-value-2"),
			Map.of("key", "test-value"));

		ResponseEntity<DeeplyImmutableRecord> echoResponse = restClient
			.post()
			.uri(echoEndpointUrl)
			.body(input)
			.retrieve()
			.toEntity(DeeplyImmutableRecord.class);

		assertThat(echoResponse.getStatusCode().value())
			.isEqualTo(200);

		final DeeplyImmutableRecord result = echoResponse.getBody();

		assertThat(result)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(input);
	}

	@Test
	public void test_echo_with_mandatory_fields() {
		final DeeplyImmutableRecord input = new DeeplyImmutableRecord(
			2,
			"text-value",
			null,
			null,
			null,
			null);

		ResponseEntity<DeeplyImmutableRecord> echoResponse = restClient
			.post()
			.uri(echoEndpointUrl)
			.body(input)
			.retrieve()
			.toEntity(DeeplyImmutableRecord.class);

		assertThat(echoResponse.getStatusCode().value())
			.isEqualTo(200);

		final DeeplyImmutableRecord result = echoResponse.getBody();

		assertThat(result)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(input);
	}
}
