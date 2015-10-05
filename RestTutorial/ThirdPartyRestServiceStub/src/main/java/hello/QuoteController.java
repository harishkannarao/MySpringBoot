package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {

    @RequestMapping("/thirdparty/quote")
    public Quote getQuote() {
        Quote dummyQuote = new Quote();
        dummyQuote.setType("success");
        Value dummyValue = new Value();
        dummyValue.setId(1L);
        dummyValue.setQuote("Working with Spring Boot is like pair-programming with the Spring developers.");
        dummyQuote.setValue(dummyValue);
        return dummyQuote;
    }
}
