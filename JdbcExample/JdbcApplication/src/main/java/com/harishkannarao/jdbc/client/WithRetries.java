package com.harishkannarao.jdbc.client;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;

@Retryable(
	maxRetriesString="#{${app.rest-client.retry.max-retry}}",
	delayString="#{${app.rest-client.retry.delay}}",
	maxDelayString="#{${app.rest-client.retry.max-delay}}",
	multiplierString="#{${app.rest-client.retry.multiplier}}",
	excludes = {
		HttpServerErrorException.InternalServerError.class
	},
	includes = {
		HttpServerErrorException.class
	})
public interface WithRetries {
}
