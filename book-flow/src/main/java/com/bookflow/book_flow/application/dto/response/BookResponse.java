package com.bookflow.book_flow.application.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class BookResponse {

  private Long id;
  private String isbn;
  private String title;
  private String subtitle;
  private String description;
  private Integer publication_year;
  private List<AuthorRoleResponse> authors;
  private List<BookGenreResponse> genres;

  // Constructors
  public BookResponse() {
  }

  public BookResponse(Long id, String isbn, String title, String subtitle, String description,
      Integer publication_year) {
    this.id = id;
    this.isbn = isbn;
    this.title = title;
    this.subtitle = subtitle;
    this.description = description;
    this.publication_year = publication_year;
  }
}
