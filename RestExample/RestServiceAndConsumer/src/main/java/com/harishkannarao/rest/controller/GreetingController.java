package com.harishkannarao.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.harishkannarao.rest.domain.Greeting;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(value = "/greeting", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(value = "/get", method = {RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Map> greetingPost(@RequestBody JsonNode input) {
        String name = input.get("name").asText();
        Map<String, String> result = new HashMap<>();
        result.put("greeting", String.format(template, name));
        return ResponseEntity.ok(result);
    }
}
