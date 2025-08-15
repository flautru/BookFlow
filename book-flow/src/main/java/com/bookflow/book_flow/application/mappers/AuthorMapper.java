package com.bookflow.book_flow.application.mappers;

import com.bookflow.book_flow.application.dto.response.AuthorResponse;
import com.bookflow.book_flow.domain.entities.Author;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

  public AuthorResponse toResponse(Author author) {
    if (author == null) {
      return null;
    }

    return new AuthorResponse(
        author.getId(),
        author.getFirstName(),
        author.getLastName(),
        author.getNationality(),
        author.getBirthDate()
    );
  }

  public List<AuthorResponse> toResponseList(List<Author> authors) {
    if (authors == null) {
      return null;
    }
    return authors.stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }
}
