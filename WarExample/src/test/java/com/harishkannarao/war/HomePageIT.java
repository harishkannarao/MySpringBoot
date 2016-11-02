package com.harishkannarao.war;

import com.harishkannarao.war.pageobjects.HomePage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class HomePageIT extends AbstractBaseIT{

    @Autowired
    private HomePage homePage;

    @Test
    public void shouldDisplayMessagesOnHomePage() throws Exception {
        homePage.navigate();
        assertEquals("Message1 from spring-boot-default-config/application.properties", homePage.getMessage1());
        assertEquals("Message2 from spring-boot-default-config/application.properties", homePage.getMessage2());
        assertEquals("Message3 from spring-boot-default-config/application.properties", homePage.getMessage3());
        assertEquals("Message4 from spring-boot-default-config/application.properties", homePage.getMessage4());
        assertEquals("Message5 from spring-boot-default-config/application.properties", homePage.getMessage5());
    }
}
