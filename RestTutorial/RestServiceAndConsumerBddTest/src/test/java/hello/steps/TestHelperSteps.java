package hello.steps;

import cucumber.api.java.en.Given;
import hello.helper.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class TestHelperSteps extends BaseStep {
    @Autowired
    private TestHelper testHelper;

    @Given("^I configure the thirdparty client urls$")
    public void configureThirdPartyClientUrls() {
        testHelper.configureThirdPartyRestQuoteClientUrl();
    }
}
