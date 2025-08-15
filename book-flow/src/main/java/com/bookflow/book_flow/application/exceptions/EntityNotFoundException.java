package com.bookflow.book_flow.application.exceptions;

public class EntityNotFoundException extends BookFlowException {

  public EntityNotFoundException(String entity, Object id) {
    super(String.format("%s not found with id: %s", entity, id));
  }
}
