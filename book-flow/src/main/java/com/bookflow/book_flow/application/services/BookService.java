package com.bookflow.book_flow.application.services;

import com.bookflow.book_flow.application.exceptions.EntityNotFoundException;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.repositories.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository bookRepository;

  public Book findById(Long id) {
    return bookRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Book", id));
  }

  public List<Book> searchAllByTitle(String title) {
    return bookRepository.findByTitleContainingIgnoreCase(title);
  }

  public Optional<Book> findByIsbn(String isbn) {
    return bookRepository.findByIsbn(isbn);
  }

  public Book save(Book book) {
    return bookRepository.save(book);
  }

  public List<Book> findAll() {
    return bookRepository.findAll();
  }

}
