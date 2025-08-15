package com.bookflow.book_flow.application.exceptions;

public abstract class BookFlowException extends RuntimeException {

  protected BookFlowException(String message) {
    super(message);
  }

  protected BookFlowException(String message, Throwable cause) {
    super(message, cause);
  }
}
