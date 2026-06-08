package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.client.ThirdPartyRestQuoteClient;
import com.harishkannarao.rest.domain.Quote;
import com.harishkannarao.rest.domain.QuoteBuilder;
import com.harishkannarao.rest.domain.ValueBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class QuoteControllerWithMockedThirdPartyClientIT extends BaseIntegration {
	@org.springframework.beans.factory.annotation.Value("${quoteEndpointUrl}")
	public String quoteEndpointUrl;

	@Autowired
	@Qualifier("myThirdPartyRestQuoteClientImpl")
	private ThirdPartyRestQuoteClient mockThirdPartyRestQuoteClient;

	@Test
	public void getQuote_shouldReturnQuoteDetails_fromThirdPartyRestService() {
		Quote expectedQuoteFromThirdPartyService = QuoteBuilder.newBuilder().setType("some type")
			.setValue(ValueBuilder.newBuilder().setId(2L).setQuote("some quote"))
			.build();
		when(mockThirdPartyRestQuoteClient.getQuote()).thenReturn(expectedQuoteFromThirdPartyService);

		Quote result = Objects.requireNonNull(testRestTemplate.method(HttpMethod.GET)
			.uri(quoteEndpointUrl)
			.exchangeSuccessfully()
			.returnResult(Quote.class)
			.getResponseBody());
		assertEquals("some type", result.getType());
		assertEquals("some quote", result.getValue().getQuote());
		assertEquals(Long.valueOf(2L), result.getValue().getId());
	}
}
