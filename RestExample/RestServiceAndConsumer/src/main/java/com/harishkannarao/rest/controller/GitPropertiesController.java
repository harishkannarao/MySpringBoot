package com.harishkannarao.rest.controller;

import com.harishkannarao.rest.util.GitPropertiesUtil;
import com.harishkannarao.rest.domain.GitProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/gitproperties", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class GitPropertiesController {

    @RequestMapping
    public GitProperties getGitProperties() {
        GitProperties gitProperties = GitPropertiesUtil.getInstance().getGitProperties();
        return gitProperties;
    }
}
