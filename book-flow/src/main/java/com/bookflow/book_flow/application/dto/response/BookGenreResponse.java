package com.bookflow.book_flow.application.dto.response;

import com.bookflow.book_flow.domain.enums.GenreIntensity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookGenreResponse {

  private Long genreId;
  private String name;
  private String description;
  private GenreIntensity intensity;
}
