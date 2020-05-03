package com.harishkannarao.jdbc.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class ApplicationErrorController extends AbstractErrorController {
    private static final boolean INCLUDE_STACK_TRACE = false;
    private static final String ERROR_PATH = "/error";
    private static final String STATUS_KEY = "status";
    private static final String ERROR_KEY = "error";
    private static final String MESSAGE_KEY = "message";

    private final Logger logger = LoggerFactory.getLogger(ApplicationErrorController.class);
    private final ErrorAttributes errorAttributes;

    @Autowired
    public ApplicationErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(value = {ERROR_PATH})
    public ResponseEntity<ErrorDetails> errorObject(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        Map<String, Object> errorAttributes = getErrorAttributes(request, INCLUDE_STACK_TRACE);
        Throwable error = this.errorAttributes.getError(new ServletWebRequest(request));
        String message = String.format("Status: %s Attributes: %s", status.value(), errorAttributes);
        logger.info(message, error);
        ErrorDetails errorDetails = new ErrorDetails(
                (Integer) errorAttributes.get(STATUS_KEY),
                (String) errorAttributes.get(ERROR_KEY),
                (String) errorAttributes.get(MESSAGE_KEY)
        );
        return new ResponseEntity<>(errorDetails, status);
    }

    public static class ErrorDetails {
        @JsonProperty("status")
        private final Integer status;
        @JsonProperty("error")
        private final String error;
        @JsonProperty("message")
        private final String message;

        @JsonCreator
        public ErrorDetails(
                @JsonProperty("status") Integer status,
                @JsonProperty("error") String error,
                @JsonProperty("message") String message
        ) {
            this.status = status;
            this.error = error;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }

}