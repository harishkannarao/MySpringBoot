package com.harishkannarao.rest.interceptor.response;

import com.harishkannarao.rest.exception.EvilHeaderException;
import com.harishkannarao.rest.exception.MyCustomCheckedException;
import com.harishkannarao.rest.exception.MyCustomRuntimeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE+1)
public class GlobalHtmlExceptionHandler {

    private static final String HTML_CONTROLLER_ERROR_VIEW = "html_controller_error";
    private static final String ERROR_MESSAGE_KEY = "errorMessage";

    @ExceptionHandler(value = {Exception.class})
    public ModelAndView handleException(HttpServletRequest request, Exception e) {
        Map<String, String> model = new HashMap<String, String>() {{
            put(ERROR_MESSAGE_KEY, e.getMessage());
        }};
        return new ModelAndView(HTML_CONTROLLER_ERROR_VIEW, model, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ModelAndView handleRuntimeException(HttpServletRequest request, Exception e) {
        Map<String, String> model = new HashMap<String, String>() {{
            put(ERROR_MESSAGE_KEY, e.getMessage());
        }};
        return new ModelAndView(HTML_CONTROLLER_ERROR_VIEW, model, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MyCustomCheckedException.class, MyCustomRuntimeException.class})
    public ModelAndView handleCustomException(HttpServletRequest request, Exception e) {
        Map<String, String> model = new HashMap<String, String>() {{
            put(ERROR_MESSAGE_KEY, e.getMessage());
        }};
        return new ModelAndView(HTML_CONTROLLER_ERROR_VIEW, model, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {EvilHeaderException.class})
    public ModelAndView handleEvilHeaderException(HttpServletRequest request, Exception e) {
        Map<String, String> model = new HashMap<String, String>() {{
            put(ERROR_MESSAGE_KEY, e.getMessage());
        }};
        return new ModelAndView(HTML_CONTROLLER_ERROR_VIEW, model, HttpStatus.BAD_REQUEST);
    }
}
