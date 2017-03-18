package com.harishkannarao.rest.interceptor.response;

import com.harishkannarao.rest.domain.ErrorResponse;
import com.harishkannarao.rest.exception.EvilHeaderException;
import com.harishkannarao.rest.exception.MyCustomCheckedException;
import com.harishkannarao.rest.exception.MyCustomRuntimeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = {RestController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalRestExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleRuntimeException(HttpServletRequest request, Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MyCustomCheckedException.class, MyCustomRuntimeException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleCustomException(HttpServletRequest request, Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        if(MyCustomCheckedException.class.isAssignableFrom(e.getClass())){
            MyCustomCheckedException myCustomCheckedException = (MyCustomCheckedException) e;
            errorResponse.setCode(myCustomCheckedException.getCode());
            errorResponse.setDescription(myCustomCheckedException.getDescription());
        } else if(MyCustomRuntimeException.class.isAssignableFrom(e.getClass())) {
            MyCustomRuntimeException myCustomRuntimeException = (MyCustomRuntimeException) e;
            errorResponse.setCode(myCustomRuntimeException.getCode());
            errorResponse.setDescription(myCustomRuntimeException.getDescription());
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {EvilHeaderException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleEvilHeaderException(HttpServletRequest request, Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        if(EvilHeaderException.class.isAssignableFrom(e.getClass())) {
            EvilHeaderException evilHeaderException = (EvilHeaderException) e;
            errorResponse.setDescription(evilHeaderException.getDescription());
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
