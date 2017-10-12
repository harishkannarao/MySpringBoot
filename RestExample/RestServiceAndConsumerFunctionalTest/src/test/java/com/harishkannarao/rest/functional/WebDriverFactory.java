package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.util.WebDriverScreenShotUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Component
public class WebDriverFactory {

    private final List<WebDriver> webDrivers = new ArrayList<>();

    public WebDriver create() {
        DesiredCapabilities phantomjsCapabilities = DesiredCapabilities.phantomjs();
        String[] phantomJsArgs = {"--ignore-ssl-errors=true", "--ssl-protocol=any"};
        phantomjsCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomJsArgs);
        WebDriver driver = new PhantomJSDriver(phantomjsCapabilities);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);

        webDrivers.add(driver);

        return driver;
    }

    public void takeScreenShots(String fileNamePrefix) {
        IntStream.range(0, webDrivers.size())
                .forEach(index -> {
                    String fileNameSufix = webDrivers.size() == 1 ? "" : "_" + index;
                    String filename = fileNamePrefix + fileNameSufix;
                    WebDriverScreenShotUtil.takeScreenShot(webDrivers.get(index), filename);
                });

    }

    public void closeDrivers() {
        webDrivers.forEach(WebDriver::quit);
        webDrivers.clear();
    }
}
