package hello;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
	private final String message;

	@Autowired
	public HelloController(@Value("${application.message:Hello World}") String message) {
		this.message = message;
	}

	@RequestMapping("/hello")
	public String sayHello(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", this.message);
		return "hello";
	}

}
