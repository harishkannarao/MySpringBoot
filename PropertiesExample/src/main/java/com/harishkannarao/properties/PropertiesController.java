package com.harishkannarao.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class PropertiesController {

	private final CustomProperties customProperties;
	private final CustomStringsProperties customStringsProperties;
	private final List<String> customString;

	@Autowired
	public PropertiesController(
		CustomProperties customProperties,
		CustomStringsProperties customStringsProperties,
		@Qualifier("customStrings") List<String> customString) {
		this.customProperties = customProperties;
		this.customStringsProperties = customStringsProperties;
		this.customString = customString;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public CustomProperties getCustomProperties() {
		return customProperties;
	}

	@RequestMapping(value = "/custom-strings", method = RequestMethod.GET)
	public CustomStringsProperties getStringListProperties() {
		return customStringsProperties;
	}

	@RequestMapping(value = "/alternate-custom-strings", method = RequestMethod.GET)
	public List<String> getAltStringListProperties() {
		return customString;
	}

}
