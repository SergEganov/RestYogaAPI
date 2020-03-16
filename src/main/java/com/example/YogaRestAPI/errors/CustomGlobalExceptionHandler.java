package com.example.YogaRestAPI.errors;

import com.example.YogaRestAPI.errors.Account.AccountNotFoundException;
import com.example.YogaRestAPI.errors.Activity.ActivityNotFoundException;
import com.example.YogaRestAPI.errors.ActivityType.ActivityTypeExistsException;
import com.example.YogaRestAPI.errors.ActivityType.ActivityTypeNotFoundException;
import com.example.YogaRestAPI.errors.Lounge.LoungeExistsException;
import com.example.YogaRestAPI.errors.Lounge.LoungeNotFoundException;
import com.example.YogaRestAPI.errors.User.UserExistsException;
import com.example.YogaRestAPI.errors.User.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({LoungeNotFoundException.class, UserNotFoundException.class, ActivityNotFoundException.class})
    public void springHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({ActivityTypeNotFoundException.class, ActivityNotFoundException.class, LoungeNotFoundException.class, UserNotFoundException.class, AccountNotFoundException.class})
    protected ResponseEntity<Object> entityNotFound(RuntimeException ex){
        Map<String, Object> body = getResponseBody(ex);
        body.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ActivityTypeExistsException.class, LoungeExistsException.class, UserExistsException.class})
    protected ResponseEntity<Object> entityExists(RuntimeException ex) {
        Map<String, Object> body = getResponseBody(ex);
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);

        return new ResponseEntity<>(body,headers,status);
    }

    private Map<String, Object> getResponseBody(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(LocalDateTime.now()));
        body.put("error", ex.getMessage());
        return body;
    }
}
