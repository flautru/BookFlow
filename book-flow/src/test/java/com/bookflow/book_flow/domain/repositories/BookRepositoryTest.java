package com.bookflow.book_flow.domain.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.bookflow.book_flow.domain.entities.Book;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private BookRepository bookRepository;

  @Test
  void save_should_generate_id_and_persist_book() {
    //Give
    Book book = createTestBook("1111111111111", "Testing", "is everithing",
        "Test is necessary in your project");

    //When
    Book savedBook = bookRepository.save(book);

    //Then
    assertThat(savedBook.getId()).isNotNull();
    assertThat(savedBook.getTitle()).isEqualTo("Testing");
    assertThat(savedBook.getIsbn()).isEqualTo("1111111111111");

    assertThat(savedBook)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(book);

  }

  @Test
  void findByIsbn_should_return_book_with_existing_book() {
    //Give
    Book book = createTestBook();
    entityManager.persistAndFlush(book);

    //When
    Optional<Book> existingBook = bookRepository.findByIsbn(book.getIsbn());

    //Then
    assertThat(existingBook).isPresent();
    assertThat(existingBook.get().getTitle()).isEqualTo(book.getTitle());
    assertThat(existingBook.get().getSubtitle()).isEqualTo(book.getSubtitle());
    assertThat(existingBook.get().getIsbn()).isEqualTo(book.getIsbn());
    assertThat(existingBook.get().getDescription()).isEqualTo(book.getDescription());
  }

  @Test
  void findByIsbn_should_return_empty_with_not_existing_isbn() {
    //Give
    String noExistingIsbn = "9999";

    //When
    Optional<Book> existingBook = bookRepository.findByIsbn(noExistingIsbn);

    //Then
    assertThat(existingBook).isEmpty();
  }

  @Test
  void findByTitle_should_return_book_with_existing_book() {
    //Give
    Book book = createTestBook();
    entityManager.persistAndFlush(book);

    //When
    Optional<Book> existingBook = bookRepository.findByTitle(book.getTitle());

    //Then
    assertThat(existingBook).isPresent();
    assertThat(existingBook.get().getTitle()).isEqualTo(book.getTitle());
    assertThat(existingBook.get().getSubtitle()).isEqualTo(book.getSubtitle());
    assertThat(existingBook.get().getIsbn()).isEqualTo(book.getIsbn());
    assertThat(existingBook.get().getDescription()).isEqualTo(book.getDescription());
  }

  @Test
  void findByTitle_should_return_empty_with_not_existing_title() {
    //Give
    String noExistingTitle = "Not existing";

    //When
    Optional<Book> existingBook = bookRepository.findByTitle(noExistingTitle);

    //Then
    assertThat(existingBook).isEmpty();
  }

  private Book createTestBook() {
    Book book = new Book();
    book.setIsbn("1111111111111");
    book.setTitle("My Test");
    book.setSubtitle("test is important");
    book.setDescription("Testing is not doubt, its important");
    return book;
  }

  private Book createTestBook(String isbn, String title, String subtitle, String description) {
    Book book = new Book();
    book.setIsbn(isbn);
    book.setTitle(title);
    book.setSubtitle(subtitle);
    book.setDescription(description);
    return book;
  }
}