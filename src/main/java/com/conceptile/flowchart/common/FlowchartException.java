package com.conceptile.flowchart.common;


public class FlowchartException extends RuntimeException {
  private final String message;

  public FlowchartException(String message) {
    super(message);
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}