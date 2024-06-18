package com.example.demo.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by David.Zheng on 17/1/21.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        Message msg = new Message("Service is not available, please contact admin with msg id");
        log.error("*************Exception occurred with id: {} ", msg.getMsgId(), exception);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(msg);
    }

    @ExceptionHandler(value = {CityNotFoundException.class})
    public ResponseEntity<?> handleCityNotFoundException(CityNotFoundException e) {
        log.info("City is not found {}.", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("City is not found"));
    }


}
