package com.harishkannarao.properties;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
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
	public void test_mock_static_with_primitive_argument() {
		assertThat(Instant.ofEpochSecond(1000L)).isEqualTo("1970-01-01T00:16:40Z");

		Instant mockedValue = Instant.parse("2024-04-01T10:20:30Z");
		try (MockedStatic<Instant> utilities = Mockito.mockStatic(Instant.class)) {
			utilities.when(() -> Instant.ofEpochSecond(Mockito.anyLong())).thenReturn(mockedValue);

			assertThat(Instant.ofEpochSecond(1000L)).isEqualTo(mockedValue);

			utilities.verify(
				() -> Instant.ofEpochSecond(Mockito.longThat(aLong -> {
					assertThat(aLong).isEqualTo(1000L);
					return true;
				})),
				times(1));
		}

		assertThat(Instant.ofEpochSecond(1000L)).isEqualTo("1970-01-01T00:16:40Z");
	}

	@Test
	public void test_mock_static_with_object_argument() {
		assertThat(LocalDateTime.now(ZoneOffset.UTC))
			.isAfterOrEqualTo(LocalDateTime.now(ZoneOffset.UTC).minusSeconds(5))
			.isBeforeOrEqualTo(LocalDateTime.now(ZoneOffset.UTC).plusSeconds(5));

		LocalDateTime mockedValue = LocalDateTime.parse("2024-12-15T14:01:37.576353");
		try (MockedStatic<LocalDateTime> utilities = Mockito.mockStatic(LocalDateTime.class)) {
			utilities.when(() -> LocalDateTime.now(Mockito.any(ZoneOffset.class))).thenReturn(mockedValue);

			assertThat(LocalDateTime.now(ZoneOffset.UTC)).isEqualTo(mockedValue);

			utilities.verify(
				() -> LocalDateTime.now(assertArg((ZoneOffset offset) -> assertThat(offset).isEqualTo(ZoneOffset.UTC))),
				times(1));
		}

		assertThat(LocalDateTime.now(ZoneOffset.UTC))
			.isAfterOrEqualTo(LocalDateTime.now(ZoneOffset.UTC).minusSeconds(5))
			.isBeforeOrEqualTo(LocalDateTime.now(ZoneOffset.UTC).plusSeconds(5));
	}
}
