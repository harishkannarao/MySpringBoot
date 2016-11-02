package com.harishkannarao.rest;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@CucumberOptions(
        strict = true,
        plugin = {
                "pretty",
                "json:target/Cucumber.json",
                "html:target/cucumber-html-report"
        }
)
@RunWith(Cucumber.class)
public class RunCukesIT {
}
