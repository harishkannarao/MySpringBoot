package com.harishkannarao.war;

import com.harishkannarao.war.pageobjects.BasePage;
import com.harishkannarao.war.pageobjects.HomePage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class BasePageIT extends AbstractBaseIT{

    @Autowired
    private BasePage basePage;
    @Autowired
    private HomePage homePage;

    @Test
    public void shouldRedirectToHomePageFromBasePage() throws Exception {
        basePage.navigate();
        assertTrue(homePage.isOnPage());
    }
}
