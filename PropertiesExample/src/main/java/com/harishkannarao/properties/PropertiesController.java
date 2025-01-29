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
	private final StringListProperties stringListProperties;

	@Autowired
	public PropertiesController(
		CustomProperties customProperties,
		StringListProperties stringListProperties) {
		this.customProperties = customProperties;
		this.stringListProperties = stringListProperties;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public CustomProperties getCustomProperties() {
		return customProperties;
	}

	@RequestMapping(value = "/list-of-string", method = RequestMethod.GET)
	public StringListProperties getStringListProperties() {
		return stringListProperties;
	}

}
