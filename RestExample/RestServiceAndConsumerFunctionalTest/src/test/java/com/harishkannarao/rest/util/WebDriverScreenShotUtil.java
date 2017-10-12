package com.harishkannarao.rest.util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class WebDriverScreenShotUtil {

    private final static Logger logger = LoggerFactory.getLogger(WebDriverScreenShotUtil.class);
    private static final String SCREENSHOT_LOCATION = "target/webdriver_screenshots/";
    private static final String EXTENSION = ".png";
    private static final String FILENAME_WITH_PATH_FORMAT = "%s%s%s";

    public static void takeScreenShot(WebDriver webDriver, String fileName) {
        if (webDriver instanceof TakesScreenshot) {
            TakesScreenshot webDriverWithScreenShotCapability = (TakesScreenshot) webDriver;
            File screenshot = webDriverWithScreenShotCapability.getScreenshotAs(OutputType.FILE);
            String fileNameWithPath = String.format(FILENAME_WITH_PATH_FORMAT, SCREENSHOT_LOCATION, fileName, EXTENSION);
            try {
                File destFile = new File(fileNameWithPath);
                logger.error("Screenshot taken: " + destFile.getAbsolutePath());
                FileUtils.copyFile(screenshot, destFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
