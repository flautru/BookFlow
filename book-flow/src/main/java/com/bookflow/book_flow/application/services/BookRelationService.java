package com.bookflow.book_flow.application.services;

import com.bookflow.book_flow.domain.entities.Author;
import com.bookflow.book_flow.domain.entities.AuthorRole;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.entities.BookGenre;
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

  public List<AuthorRole> findAuthorsByBook(Book book) {
    return authorRoleRepository.findByBook(book);
  }

  public List<Book> findBooksByGenre(Genre genre) {
    return bookGenreRepository.findBooksByGenre(genre);
  }

  public List<BookGenre> findGenresByBook(Book book) {
    return bookGenreRepository.findByBook(book);
  }
}
