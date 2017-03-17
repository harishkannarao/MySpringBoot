package com.harishkannarao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class ApplicationErrorController extends AbstractErrorController {
    private static final String ERROR_PATH = "/error";
    private static final boolean INCLUDE_STACK_TRACE = true;
    private static final String ERROR_VIEW = "error";

    @Autowired
    public ApplicationErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(value = {ERROR_PATH})
    public ModelAndView errorHtml(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, INCLUDE_STACK_TRACE);
        return new ModelAndView(ERROR_VIEW, errorAttributes, getStatus(request));
    }
}
