package com.bookflow.book_flow.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

  @NotBlank(message = "ISBN is required")
  @Size(min = 13, max = 13, message = "ISBN must be exactly 13 characters")
  private String isbn;

  @NotBlank(message = "Title is required")
  @Size(max = 255, message = "Title must not exceed 255 characters")
  private String title;

  @Size(max = 255, message = "Subtitle must not exceed 255 characters")
  private String subtitle;

  @Size(max = 5000, message = "Description must not exceed 5000 characters")
  private String description;

  private Integer publication_year;
}
