package hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Map;

@Controller
public class HelloController {
	private final String message;

	@Autowired
	public HelloController(@Value("${application.message:Hello World}") String message) {
		this.message = message;
	}

	@RequestMapping("/hello")
	public String sayHello(Map<String, Object> model) {
		model.put("date", LocalDate.now());
		model.put("message", this.message);
		return "hello";
	}

}
