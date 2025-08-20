package com.bookflow.book_flow.application.mappers;

import com.bookflow.book_flow.application.dto.request.BookRequest;
import com.bookflow.book_flow.application.dto.response.BookResponse;
import com.bookflow.book_flow.domain.entities.Book;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

  private final AuthorRoleMapper authorRoleMapper;
  private final BookGenreMapper bookGenreMapper;

  public BookMapper(AuthorRoleMapper authorRoleMapper, BookGenreMapper bookGenreMapper) {
    this.authorRoleMapper = authorRoleMapper;
    this.bookGenreMapper = bookGenreMapper;
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
        book.getDescription(),
        book.getPublication_year()
    );
  }

  public BookResponse toResponseWithRelations(Book book) {
    BookResponse response = toResponse(book);
    if (response != null) {
      response.setAuthors(
          book.getAuthorRoles().stream()
              .map(authorRoleMapper::toResponse)
              .collect(Collectors.toList())
      );
      response.setGenres(
          book.getBookGenres().stream()
              .map(bookGenreMapper::toResponse)
              .collect(Collectors.toList())
      );
    }
    return response;
  }

  public List<BookResponse> toResponseList(List<Book> books) {
    if (books == null) {
      return null;
    }
    return books.stream()
        .map(this::toResponseWithRelations)
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