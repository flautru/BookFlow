package com.bookflow.book_flow.application.exceptions;

public class DuplicateResourceException extends BookFlowException {

  public DuplicateResourceException(String resource, String field, Object value) {
    super(String.format("%s already exists with %s: %s", resource, field, value));
  }
}
