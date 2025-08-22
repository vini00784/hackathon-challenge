package com.vini.hackathon.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorWrapper> handleBusinessException(BusinessException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus().toString(), ex.getMessage());
        ErrorWrapper errorWrapper = new ErrorWrapper(errorResponse);
        return ResponseEntity.status(ex.getStatus()).body(errorWrapper);
    }

}
