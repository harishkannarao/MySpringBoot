package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.security.AuthContext;
import com.harishkannarao.jdbc.security.Caller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.harishkannarao.jdbc.configuration.OpenApiConfiguration.COOKIE_AUTH_SCHEME_KEY;
import static com.harishkannarao.jdbc.configuration.OpenApiConfiguration.HEADER_AUTH_SCHEME_KEY;

@RestController
@RequestMapping(value = "/auth", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ExampleAuthRestController {

    private final AuthContext authContext;

    @Autowired
    public ExampleAuthRestController(AuthContext authContext) {
        this.authContext = authContext;
    }

    @GetMapping(value = "/header")
    @Operation(security = {
            @SecurityRequirement(name = HEADER_AUTH_SCHEME_KEY),
            @SecurityRequirement(name = COOKIE_AUTH_SCHEME_KEY)
    })
    public ResponseEntity<Caller> getCallerFromHeader(HttpServletRequest request) {
        Caller caller = authContext.verifyRoleAndGetCaller(request, "header-role");
        return ResponseEntity.ok(caller);
    }

    @GetMapping(value = "/cookie")
    @Operation(security = {
            @SecurityRequirement(name = HEADER_AUTH_SCHEME_KEY),
            @SecurityRequirement(name = COOKIE_AUTH_SCHEME_KEY)
    })
    public ResponseEntity<Caller> getCallerFromCookie(HttpServletRequest request) {
        Caller caller = authContext.verifyRoleAndGetCaller(request, "cookie-role");
        return ResponseEntity.ok(caller);
    }
}
