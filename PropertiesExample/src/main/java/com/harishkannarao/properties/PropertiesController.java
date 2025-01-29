package com.harishkannarao.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class PropertiesController {

	private final CustomProperties customProperties;
	private final CustomStringsProperties customStringsProperties;

	@Autowired
	public PropertiesController(
		CustomProperties customProperties,
		CustomStringsProperties customStringsProperties) {
		this.customProperties = customProperties;
		this.customStringsProperties = customStringsProperties;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public CustomProperties getCustomProperties() {
		return customProperties;
	}

	@RequestMapping(value = "/custom-strings", method = RequestMethod.GET)
	public CustomStringsProperties getStringListProperties() {
		return customStringsProperties;
	}

}
