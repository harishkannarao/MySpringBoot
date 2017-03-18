package com.harishkannarao.rest.controller;

import com.harishkannarao.rest.exception.MyCustomCheckedException;
import com.harishkannarao.rest.exception.MyCustomRuntimeException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "generate-html-exception")
public class HtmlExceptionSimulationController {

    @RequestMapping(path = "runtime")
    public void generateRuntimeException() {
        throw new RuntimeException("My Sample Runtime Exception");
    }

    @RequestMapping(path = "checked")
    public void generateCheckedException() throws Exception {
        throw new Exception("My Sample Checked Exception");
    }

    @RequestMapping(path = "custom-runtime")
    public void generateCustomRuntimeException() {
        throw new MyCustomRuntimeException("CustomRuntime", "My Custom Runtime Exception");
    }

    @RequestMapping(path = "custom-checked")
    public void generateCustomCheckedException() throws Exception {
        throw new MyCustomCheckedException("CustomChecked", "My Custom Checked Exception");
    }

}
