package com.bookflow.book_flow.application.exceptions;

public class BusinessRuleViolationException extends BookFlowException {

  public BusinessRuleViolationException(String rule) {
    super(String.format("Business rule violation: %s", rule));
  }
}
