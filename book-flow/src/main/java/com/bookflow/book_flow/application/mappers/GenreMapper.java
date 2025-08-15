package com.bookflow.book_flow.application.mappers;

import com.bookflow.book_flow.application.dto.response.GenreResponse;
import com.bookflow.book_flow.domain.entities.Genre;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

  public GenreResponse toResponse(Genre genre) {
    if (genre == null) {
      return null;
    }

    return new GenreResponse(
        genre.getId(),
        genre.getName(),
        genre.getDescription()
    );
  }

  public List<GenreResponse> toResponseList(List<Genre> genres) {
    if (genres == null) {
      return null;
    }
    return genres.stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }
}
