package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "generate-exception")
public class ExceptionSimulationController {

    @RequestMapping(path = "runtime")
    public void generateRuntimeException() {
        throw new RuntimeException("My Sample Runtime Exception");
    }

    @RequestMapping(path = "checked")
    public void generateCheckedException() throws Exception {
        throw new Exception("My Sample Checked Exception");
    }

    @RequestMapping(path = "custom-runtime")
    public void generateCustomRuntimeException() {
        throw new MyCustomRuntimeException("CustomRuntime", "My Custom Runtime Exception");
    }

    @RequestMapping(path = "custom-checked")
    public void generateCustomCheckedException() throws Exception {
        throw new MyCustomCheckedException("CustomChecked", "My Custom Checked Exception");
    }

    public static class MyCustomRuntimeException extends RuntimeException {
        private final String code;
        private final String description;


        public MyCustomRuntimeException(String code, String description) {
            super(code + ":" + description);
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class MyCustomCheckedException extends Exception {
        private final String code;
        private final String description;

        public MyCustomCheckedException(String code, String description) {
            super(code + ":" + description);
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
