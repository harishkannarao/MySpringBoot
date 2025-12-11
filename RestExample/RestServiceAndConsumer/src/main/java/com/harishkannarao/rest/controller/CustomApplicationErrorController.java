package com.harishkannarao.rest.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.harishkannarao.rest.exception.MyCustomRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.webmvc.autoconfigure.error.BasicErrorController;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorViewResolver;
import org.springframework.boot.webmvc.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class CustomApplicationErrorController extends BasicErrorController {
    private static final String GENERAL_ERROR_VIEW = "general_error";
    private static final String STATUS_KEY = "status";
    private static final String ERROR_KEY = "error";

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

    @RequestMapping(produces = {MediaType.TEXT_HTML_VALUE})
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> errorAttributes = this.getErrorAttributes(request, this.getErrorAttributeOptions(request, MediaType.ALL));
        if (HttpStatus.NOT_FOUND.equals(status)) {
            logger.debug(errorAttributes.get(ERROR_KEY).toString());
        } else {
            logger.error(errorAttributes.get(ERROR_KEY).toString());
        }
        return new ModelAndView(GENERAL_ERROR_VIEW, errorAttributes, status);
    }

    @RequestMapping
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        Map<String, Object> errorAttributes = this.getErrorAttributes(request, this.getErrorAttributeOptions(request, MediaType.ALL));
        Throwable error = this.errorAttributes.getError(new ServletWebRequest(request));
        if (error != null) {
            if (error instanceof MyCustomRuntimeException) {
                MyCustomRuntimeException exception = (MyCustomRuntimeException) error;
                Map<String, Object> errorDetails = Map.ofEntries(
                        Map.entry("status", HttpStatus.METHOD_NOT_ALLOWED.value()),
                        Map.entry("error", String.format("%s :: %s", exception.getCode(), exception.getDescription()))
                );
                return new ResponseEntity<>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
            }
        }
        Map<String, Object> errorDetails = Map.ofEntries(
                Map.entry("status", errorAttributes.get(STATUS_KEY)),
                Map.entry("error", errorAttributes.get(ERROR_KEY))
        );
        if (HttpStatus.NOT_FOUND.equals(status)) {
            logger.debug(errorAttributes.get(ERROR_KEY).toString());
        } else {
            logger.error(errorAttributes.get(ERROR_KEY).toString());
        }
        return new ResponseEntity<>(errorDetails, status);
    }

}
