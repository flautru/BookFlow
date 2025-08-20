package com.bookflow.book_flow.infrastructure.exceptions;

import com.bookflow.book_flow.application.dto.response.ErrorResponse;
import com.bookflow.book_flow.application.exceptions.BusinessRuleViolationException;
import com.bookflow.book_flow.application.exceptions.DuplicateResourceException;
import com.bookflow.book_flow.application.exceptions.EntityNotFoundException;
import com.bookflow.book_flow.application.exceptions.InvalidReferenceException;
import com.bookflow.book_flow.domain.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(
      EntityNotFoundException ex,
      HttpServletRequest request) {

    logger.warn("Entity not found: {}", ex.getMessage());

    ErrorResponse errorResponse = createErrorResponse(
        ex.getMessage(),
        ErrorCode.ENTITY_NOT_FOUND,
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateResource(
      DuplicateResourceException ex,
      HttpServletRequest request) {

    logger.warn("Duplicate resource: {}", ex.getMessage());

    ErrorResponse errorResponse = createErrorResponse(
        ex.getMessage(),
        ErrorCode.DUPLICATE_RESOURCE,
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(InvalidReferenceException.class)
  public ResponseEntity<ErrorResponse> handleInvalidReference(
      InvalidReferenceException ex,
      HttpServletRequest request) {

    logger.warn("Invalid reference: {}", ex.getMessage());

    ErrorResponse errorResponse = createErrorResponse(
        ex.getMessage(),
        ErrorCode.INVALID_REFERENCE,
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(BusinessRuleViolationException.class)
  public ResponseEntity<ErrorResponse> handleBusinessRuleViolation(
      BusinessRuleViolationException ex,
      HttpServletRequest request) {

    logger.warn("Business rule violation: {}", ex.getMessage());

    ErrorResponse errorResponse = createErrorResponse(
        ex.getMessage(),
        ErrorCode.BUSINESS_RULE_VIOLATION,
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {

    logger.warn("Validation failed: {} field errors", ex.getBindingResult().getFieldErrorCount());

    Map<String, String> validationErrors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      if (error instanceof FieldError fieldError) {
        validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
      } else {
        validationErrors.put("global", error.getDefaultMessage());
      }
    });

    String generalMessage = String.format("Validation failed for %d field(s)",
        validationErrors.size());

    ErrorResponse errorResponse = createErrorResponse(
        generalMessage,
        ErrorCode.VALIDATION_FAILED,
        request.getRequestURI(),
        validationErrors
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
      DataIntegrityViolationException ex,
      HttpServletRequest request) {

    logger.error("Data integrity violation: {}", ex.getMessage());

    String message = "Data integrity constraint violation";
    HttpStatus status = HttpStatus.CONFLICT;
    ErrorCode errorCode = ErrorCode.DUPLICATE_RESOURCE;

    String rootMessage =
        ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

    if (rootMessage != null) {
      if (rootMessage.contains("duplicate key") || rootMessage.contains("unique constraint")) {
        message = "Resource already exists with the provided unique field";
        status = HttpStatus.CONFLICT;
        errorCode = ErrorCode.DUPLICATE_RESOURCE;
      } else if (rootMessage.contains("foreign key") || rootMessage.contains("violates not-null")) {
        message = "Invalid reference or missing required field";
        status = HttpStatus.BAD_REQUEST;
        errorCode = ErrorCode.INVALID_REFERENCE;
      }
    }

    ErrorResponse errorResponse = createErrorResponse(message, errorCode, request.getRequestURI());

    return ResponseEntity.status(status).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex,
      HttpServletRequest request) {

    logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);

    ErrorResponse errorResponse = createErrorResponse(
        "An unexpected error occurred. Please try again later.",
        ErrorCode.INTERNAL_ERROR,
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  private ErrorResponse createErrorResponse(String message, ErrorCode errorCode, String path) {
    return createErrorResponse(message, errorCode, path, null);
  }

  private ErrorResponse createErrorResponse(
      String message,
      ErrorCode errorCode,
      String path,
      Map<String, String> validationErrors) {

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setMessage(message);
    errorResponse.setErrorCode(errorCode);
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setPath(path);

    if (validationErrors != null && !validationErrors.isEmpty()) {
      errorResponse.setValidationErrors(validationErrors);
    }

    return errorResponse;
  }
}