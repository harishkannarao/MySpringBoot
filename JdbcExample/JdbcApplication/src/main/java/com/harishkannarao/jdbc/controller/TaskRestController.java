package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.domain.TaskSummary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@RestController
@RequestMapping(value = "/task-summary", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TaskRestController extends AbstractBaseController {

    @RequestMapping(method = RequestMethod.GET)
    public List<TaskSummary> getAllTaskSummaries() {
        return List.of(
                new TaskSummary("1", "First Task"),
                new TaskSummary("2", "Second Task")
        );
    }
}
