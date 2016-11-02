package com.harishkannarao.war;

import com.harishkannarao.war.pageobjects.HomePageDevProfile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class HomePageDevProfileIT extends AbstractDevProfileBaseIT {

    @Autowired
    private HomePageDevProfile homePageDevProfile;

    @Test
    public void shouldDisplayOverriddenMessagesOnHomePage() throws Exception {
        homePageDevProfile.navigate();
        assertEquals("Message1 from spring-boot-default-config/application.properties", homePageDevProfile.getMessage1());
        assertEquals("Message2 from spring-boot-default-config/application.properties", homePageDevProfile.getMessage2());
        assertEquals("Message3 from spring-boot-default-config/application.properties", homePageDevProfile.getMessage3());
        assertEquals("Message4 from spring-boot-default-config/application.properties", homePageDevProfile.getMessage4());
        assertEquals("Message5 from spring-boot-default-config/application-dev.properties", homePageDevProfile.getMessage5());
    }
}
