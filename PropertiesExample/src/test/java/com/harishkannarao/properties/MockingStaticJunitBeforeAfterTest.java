package com.harishkannarao.properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.times;

public class MockingStaticJunitBeforeAfterTest {
	private MockedStatic<Instant> mockInstant;
	private MockedStatic<LocalDateTime> mockLocalDateTime;

	@BeforeEach
	public void setUpMockInstant() {
		mockInstant = Mockito.mockStatic(Instant.class);
	}

	@AfterEach
	public void tearDownMockInstant() {
		mockInstant.close();
	}

	@BeforeEach
	public void setUpMockLocalDateTime() {
		mockLocalDateTime = Mockito.mockStatic(LocalDateTime.class);
	}

	@AfterEach
	public void tearDownMockLocalDateTime() {
		mockLocalDateTime.close();
	}

	@Test
	public void test_mock_static_without_arguments() {
		Instant mockedValue = Instant.parse("2024-04-01T10:20:30Z");
		mockInstant.when(Instant::now).thenReturn(mockedValue);
		assertThat(Instant.now()).isEqualTo(mockedValue);
	}

	@Test
	public void test_mock_static_with_primitive_argument() {
		Instant mockedValue = Instant.parse("2024-04-01T10:20:30Z");
		mockInstant.when(() -> Instant.ofEpochSecond(Mockito.anyLong())).thenReturn(mockedValue);

		assertThat(Instant.ofEpochSecond(1000L)).isEqualTo(mockedValue);

		mockInstant.verify(
			() -> Instant.ofEpochSecond(Mockito.longThat(aLong -> {
				assertThat(aLong).isEqualTo(1000L);
				return true;
			})),
			times(1));
	}

	@Test
	public void test_mock_static_with_object_argument() {
		LocalDateTime mockedValue = LocalDateTime.parse("2024-12-15T14:01:37.576353");
		mockLocalDateTime.when(() -> LocalDateTime.now(Mockito.any(ZoneOffset.class))).thenReturn(mockedValue);

		assertThat(LocalDateTime.now(ZoneOffset.UTC)).isEqualTo(mockedValue);

		mockLocalDateTime.verify(
			() -> LocalDateTime.now(assertArg((ZoneOffset offset) -> assertThat(offset).isEqualTo(ZoneOffset.UTC))),
			times(1));
	}
}
