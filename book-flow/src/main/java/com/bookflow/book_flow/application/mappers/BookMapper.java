package com.bookflow.book_flow.application.mappers;

import com.bookflow.book_flow.application.dto.request.BookRequest;
import com.bookflow.book_flow.application.dto.response.BookResponse;
import com.bookflow.book_flow.domain.entities.AuthorRole;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.entities.BookGenre;
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

  public BookResponse toResponseWithRelations(Book book,
      List<AuthorRole> authorRoles,
      List<BookGenre> bookGenres) {
    BookResponse response = toResponse(book);
    if (response != null) {
      response.setAuthors(authorRoleMapper.toResponseList(authorRoles));
      response.setGenres(bookGenreMapper.toResponseList(bookGenres));
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