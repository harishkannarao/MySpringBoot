package com.harishkannarao.rest.functional;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

public class HtmlFormPageIT extends BaseIntegration {

    @Value("${htmlFormPageEndpointUrl}")
    public String htmlFormPageEndpointUrl;

    @Test
    public void shouldGetIndexPage() {
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(htmlFormPageEndpointUrl);

        // verify no errors are displayed
        List<WebElement> errorElements = webDriver.findElements(By.id("error"));
        assertThat(errorElements, empty());

        // verify default message in name field
        WebElement nameElement = webDriver.findElement(By.id("name"));
        String defaultName = nameElement.getAttribute("value");
        assertThat(defaultName, equalTo("Enter your name"));

        // set new values
        nameElement.clear();
        nameElement.sendKeys("Test Name");
        webDriver.findElement(By.id("cats")).click();

        // submit form
        webDriver.findElement(By.id("submit-form")).click();

        // verify error
        String errorMessage = webDriver.findElement(By.id("error")).getText();
        assertThat(errorMessage, equalTo("Please select at least 2 pets"));

        // verify already entered values
        String name = webDriver.findElement(By.id("name")).getAttribute("value");
        assertThat(name, equalTo("Test Name"));
        boolean catsSelected = webDriver.findElement(By.id("cats")).isSelected();
        assertThat(catsSelected, equalTo(true));

        // select another pet
        webDriver.findElement(By.id("birds")).click();

        // submit form again
        webDriver.findElement(By.id("submit-form")).click();

        // verify success page
        String successMessage = webDriver.findElement(By.id("message")).getText();
        assertThat(successMessage, equalTo("For submission success !!!"));

        // restart form
        webDriver.findElement(By.id("restart")).click();

        // verify form
        String newName = webDriver.findElement(By.id("name")).getAttribute("value");
        assertThat(newName, equalTo("Enter your name"));
    }
}
