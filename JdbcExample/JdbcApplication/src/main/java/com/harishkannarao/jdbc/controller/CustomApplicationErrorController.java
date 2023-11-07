package com.harishkannarao.jdbc.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;
import java.util.Map;

@Controller
public class CustomApplicationErrorController extends BasicErrorController {
    private static final String STATUS_KEY = "status";
    private static final String ERROR_KEY = "error";
    private static final String MESSAGE_KEY = "message";

    private final Logger logger = LoggerFactory.getLogger(CustomApplicationErrorController.class);
    private final ErrorAttributes errorAttributes;

    @Autowired
    public CustomApplicationErrorController(ErrorAttributes errorAttributes, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, createErrorProperties(), errorViewResolvers);
        this.errorAttributes = errorAttributes;
    }

    private static ErrorProperties createErrorProperties() {
        ErrorProperties errorProperties = new ErrorProperties();
        errorProperties.setIncludeStacktrace(ErrorProperties.IncludeAttribute.ALWAYS);
        errorProperties.setIncludeException(true);
        errorProperties.setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
        errorProperties.setIncludeBindingErrors(ErrorProperties.IncludeAttribute.ALWAYS);
        return errorProperties;
    }

    @RequestMapping
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        Map<String, Object> errorAttributes = this.getErrorAttributes(request, this.getErrorAttributeOptions(request, MediaType.ALL));
        Throwable error = this.errorAttributes.getError(new ServletWebRequest(request));
        String message = String.format("Status: %s Attributes: %s", status.value(), errorAttributes);
        logger.info(message, error);
        Map<String, Object> errorDetails = Map.ofEntries(
                Map.entry("status", errorAttributes.get(STATUS_KEY)),
                Map.entry("error", errorAttributes.get(ERROR_KEY)),
                Map.entry("message", errorAttributes.get(MESSAGE_KEY))
        );
        return new ResponseEntity<>(errorDetails, status);
    }
}