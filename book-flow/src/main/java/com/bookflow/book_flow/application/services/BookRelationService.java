package com.bookflow.book_flow.application.services;

import com.bookflow.book_flow.domain.entities.Author;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.entities.Genre;
import com.bookflow.book_flow.domain.repositories.AuthorRoleRepository;
import com.bookflow.book_flow.domain.repositories.BookGenreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookRelationService {

  private final AuthorRoleRepository authorRoleRepository;
  private final BookGenreRepository bookGenreRepository;

  public List<Book> findBooksByAuthor(Author author) {
    return authorRoleRepository.findBooksByAuthor(author);
  }

  public List<Author> findAuthorsByBook(Book book) {
    return authorRoleRepository.findAuthorsByBook(book);
  }

  public List<Book> findBooksByGenre(Genre genre) {
    return bookGenreRepository.findBooksByGenre(genre);
  }

  public List<Genre> findGenresByBook(Book book) {
    return bookGenreRepository.findGenresByBook(book);
  }
}
