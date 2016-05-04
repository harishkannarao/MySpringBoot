package hello;

import hello.pageobjects.BasePage;
import hello.pageobjects.HomePage;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class BasePageIT extends AbstractBaseIT{

    @Autowired
    private BasePage basePage;
    @Autowired
    private HomePage homePage;

    @Test
    @Ignore
    public void shouldRedirectToIndexPageFromBasePage() throws Exception {
        basePage.navigate();
        assertTrue(homePage.isOnPage());
    }
}
