package com.harishkannarao.properties;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

public class MockingStaticExampleTest {

	@Test
	public void test_mock_static_without_arguments() {
		Instant plusFive = Instant.now().plusSeconds(5);
		Instant minusFive = Instant.now().minusSeconds(5);

		assertThat(Instant.now())
			.isBeforeOrEqualTo(plusFive)
			.isAfterOrEqualTo(minusFive);

		Instant mockedValue = Instant.parse("2024-04-01T10:20:30Z");
		try (MockedStatic<Instant> utilities = Mockito.mockStatic(Instant.class)) {
			utilities.when(Instant::now).thenReturn(mockedValue);
			assertThat(Instant.now()).isEqualTo(mockedValue);
		}

		assertThat(Instant.now())
			.isBeforeOrEqualTo(plusFive)
			.isAfterOrEqualTo(minusFive);
	}

	@Test
	public void test_mock_static_with_arguments() {
		assertThat(Instant.ofEpochSecond(1000L)).isEqualTo("1970-01-01T00:16:40Z");

		Instant mockedValue = Instant.parse("2024-04-01T10:20:30Z");
		try (MockedStatic<Instant> utilities = Mockito.mockStatic(Instant.class)) {
			utilities.when(() -> Instant.ofEpochSecond(Mockito.eq(1000L))).thenReturn(mockedValue);
			assertThat(Instant.ofEpochSecond(1000L)).isEqualTo(mockedValue);

			utilities.verify(
				() -> Instant.ofEpochSecond(Mockito.eq(1000L)),
				times(1));
		}

		assertThat(Instant.ofEpochSecond(1000L)).isEqualTo("1970-01-01T00:16:40Z");
	}
}
