package com.bookflow.book_flow.application.dto.response;

import com.bookflow.book_flow.domain.enums.ErrorCode;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;

@Data
public class ErrorResponse {

  private String message;
  private ErrorCode errorCode;
  private LocalDateTime timestamp;
  private String path;
  private Map<String, String> validationErrors; // Pour @Valid

}
