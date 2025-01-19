package com.harishkannarao.jdbc.client;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;

@Retryable(
	maxAttemptsExpression="#{${app.rest-client.retry.max-retry}}",
	backoff=@Backoff(
		delayExpression="#{${app.rest-client.retry.delay}}",
		maxDelayExpression="#{${app.rest-client.retry.max-delay}}",
		multiplierExpression = "#{${app.rest-client.retry.multiplier}}"),
	noRetryFor = {
		HttpServerErrorException.InternalServerError.class
	},
	retryFor = {
		HttpServerErrorException.class
	})
public interface WithRetries {
}
