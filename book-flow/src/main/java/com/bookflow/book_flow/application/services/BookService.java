package com.bookflow.book_flow.application.services;

import com.bookflow.book_flow.application.exceptions.EntityNotFoundException;
import com.bookflow.book_flow.application.validators.BookValidator;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.repositories.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

  private static final Logger logger = LoggerFactory.getLogger(BookService.class);
  private final BookRepository bookRepository;
  private final BookValidator bookValidator;

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
    bookValidator.validate(book);
    return bookRepository.save(book);
  }

  @Transactional(readOnly = true)
  public List<Book> findAll() {
    if (logger.isDebugEnabled()) {
      long startTime = System.currentTimeMillis();
      List<Book> books = bookRepository.findAllWithRelations();
      long duration = System.currentTimeMillis() - startTime;
      logger.debug("📊 PERFORMANCE: findAll() took {}ms for {} books", duration, books.size());

      return books;
    }

    return bookRepository.findAllWithRelations();
  }

}
