package com.harishkannarao.jdbc;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HomePageIT extends BaseIntegrationJdbc {

    @Value("${homePageEndpointUrl}")
    public String homePageEndpointUrl;

    @Test
    public void should_display_utc_date_time() {
        OffsetDateTime startTime = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(homePageEndpointUrl + "?displayDate=true");
        String date = webDriver.findElement(By.id("utc_date")).getText();
        OffsetDateTime endTime = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);
        OffsetDateTime actual = OffsetDateTime.parse(date);
        assertThat(actual, greaterThanOrEqualTo(startTime));
        assertThat(actual, lessThanOrEqualTo(endTime));
    }

    @Test
    public void should_not_display_utc_date_time() {
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(homePageEndpointUrl);
        List<WebElement> result = webDriver.findElements(By.id("utc_date"));
        assertThat(result, empty());
    }
}
