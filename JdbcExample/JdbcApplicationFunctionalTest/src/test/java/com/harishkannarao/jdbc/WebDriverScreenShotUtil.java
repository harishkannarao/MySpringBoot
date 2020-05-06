package com.harishkannarao.jdbc;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class WebDriverScreenShotUtil {
    private static final String SCREENSHOT_LOCATION = "target/webdriver_screenshots";
    private static final String EXTENSION = ".png";
    private static final String FILENAME_WITH_PATH_FORMAT = "%s%s%s%s";

    public static void takeScreenShot(WebDriver webDriver, String fileName, boolean success) {
        if (webDriver instanceof TakesScreenshot) {
            TakesScreenshot webDriverWithScreenShotCapability = (TakesScreenshot) webDriver;
            File screenshot = webDriverWithScreenShotCapability.getScreenshotAs(OutputType.FILE);
            String suffix = success ? "/success/" : "/failure/";
            String fileNameWithPath = String.format(FILENAME_WITH_PATH_FORMAT, SCREENSHOT_LOCATION, suffix, fileName, EXTENSION);
            try {
                FileUtils.copyFile(screenshot, new File(fileNameWithPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
