package com.conceptile.flowchart.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ResponseEntityExceptionHandler {

  @ExceptionHandler(FlowchartException.class)
  public ResponseEntity<Object> handleFlowchartException(FlowchartException ex) {
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("Error: ", ex.getMessage()));
  }
}
