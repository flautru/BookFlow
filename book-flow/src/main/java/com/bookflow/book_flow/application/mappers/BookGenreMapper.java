package com.bookflow.book_flow.application.mappers;

import com.bookflow.book_flow.application.dto.response.BookGenreResponse;
import com.bookflow.book_flow.domain.entities.BookGenre;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class BookGenreMapper {

  public BookGenreResponse toResponse(BookGenre genre) {
    if (genre == null) {
      return null;
    }

    return new BookGenreResponse(
        genre.getId(),
        genre.getGenre().getName(),
        genre.getGenre().getDescription(),
        genre.getIntensity()
    );
  }

  public List<BookGenreResponse> toResponseList(List<BookGenre> genres) {
    if (genres == null) {
      return null;
    }
    return genres.stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }
}