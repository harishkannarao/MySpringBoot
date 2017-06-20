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

    private final List<WebDriverWrapper> webDrivers = new ArrayList<>();

    public WebDriver create(String testDisplayName) {
        DesiredCapabilities phantomjsCapabilities = DesiredCapabilities.phantomjs();
        String[] phantomJsArgs = {"--ignore-ssl-errors=true", "--ssl-protocol=any"};
        phantomjsCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomJsArgs);
        WebDriver driver = new PhantomJSDriver(phantomjsCapabilities);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);

        webDrivers.add(new WebDriverWrapper(testDisplayName, driver));

        return driver;
    }

    public void closeDrivers() {
        IntStream.range(0, webDrivers.size())
                .forEach(index -> {
                    WebDriverWrapper webDriverWrapper = webDrivers.get(index);
                    String filename = webDriverWrapper.getTestDisplayName() + "_" + index;
                    WebDriverScreenShotUtil.takeScreenShot(webDriverWrapper.getWebDriver(), filename);
                    webDriverWrapper.getWebDriver().quit();
                });
        webDrivers.clear();
    }
}
