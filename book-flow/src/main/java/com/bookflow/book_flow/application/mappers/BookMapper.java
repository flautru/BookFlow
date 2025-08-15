package com.bookflow.book_flow.application.mappers;

import com.bookflow.book_flow.application.dto.request.BookRequest;
import com.bookflow.book_flow.application.dto.response.BookResponse;
import com.bookflow.book_flow.domain.entities.Book;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

  private final AuthorMapper authorMapper;
  private final GenreMapper genreMapper;

  public BookMapper(AuthorMapper authorMapper, GenreMapper genreMapper) {
    this.authorMapper = authorMapper;
    this.genreMapper = genreMapper;
  }

  public BookResponse toResponse(Book book) {
    if (book == null) {
      return null;
    }

    return new BookResponse(
        book.getId(),
        book.getIsbn(),
        book.getTitle(),
        book.getSubtitle(),
        book.getDescription()
    );
  }

  public BookResponse toResponseWithRelations(Book book,
      List<com.bookflow.book_flow.domain.entities.Author> authors,
      List<com.bookflow.book_flow.domain.entities.Genre> genres) {
    BookResponse response = toResponse(book);
    if (response != null) {
      response.setAuthors(authorMapper.toResponseList(authors));
      response.setGenres(genreMapper.toResponseList(genres));
    }
    return response;
  }

  public List<BookResponse> toResponseList(List<Book> books) {
    if (books == null) {
      return null;
    }
    return books.stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  public Book toEntity(BookRequest request) {
    if (request == null) {
      return null;
    }

    Book book = new Book();
    book.setIsbn(request.getIsbn());
    book.setTitle(request.getTitle());
    book.setSubtitle(request.getSubtitle());
    book.setDescription(request.getDescription());
    return book;
  }
}