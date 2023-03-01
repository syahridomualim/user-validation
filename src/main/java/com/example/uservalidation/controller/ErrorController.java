package com.example.uservalidation.controller;

import com.example.uservalidation.model.Response;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {

    /*
    * this exception invoke when input invalid
    * */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, List<String>> errorMessages = new HashMap<>();
        val message = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
        errorMessages.put("errors", message);
        val response = new Response(
                new Date(), BAD_REQUEST.getReasonPhrase(), BAD_REQUEST.value(), errorMessages
        );
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    /*
    * this exception invoke when element doesn't exist in database
    * */
    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<Object> notFoundException(NoSuchElementException e) {
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("errors", e.getMessage());
        val response = new Response(
                new Date(), NOT_FOUND.getReasonPhrase(), NOT_FOUND.value(), errorMessages
        );
        return new ResponseEntity<>(response, NOT_FOUND);
    }
}
