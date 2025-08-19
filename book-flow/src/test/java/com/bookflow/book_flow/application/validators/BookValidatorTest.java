package com.bookflow.book_flow.application.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bookflow.book_flow.application.exceptions.InvalidReferenceException;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.repositories.BookRepository;
import com.bookflow.book_flow.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookValidatorTest {

  @Mock
  private BookRepository bookRepository;

  private BookValidator validator;

  @BeforeEach
  void setUp() {
    validator = new BookValidator(bookRepository);
  }

  @Test
  void validate_should_succeed_with_valid_isbn_checksum() {
    Book book = TestDataFactory.createTestBook("9785412000046", "Nous les dieux", "",
        "Premier volet cycle des dieux");
    validator.validate(book);

  }

  @Test
  void validate_should_throw_exception_with_invalid_isbn_format_letters() {
    Book book = TestDataFactory.createTestBook("9785zd2000046", "Nous les dieux", "",
        "Premier volet cycle des dieux");

    InvalidReferenceException exception = assertThrows(
        InvalidReferenceException.class,
        () -> validator.validate(book)
    );

    assertThat(exception.getMessage()).isEqualTo(
        "ISBN must contain exactly 13 digits without spaces or dashes");
  }

  @Test
  void validate_should_throw_exception_with_invalid_isbn_checksum() {
    Book book = TestDataFactory.createTestBook("9785412000047", "Nous les dieux", "",
        "Premier volet cycle des dieux");

    InvalidReferenceException exception = assertThrows(
        InvalidReferenceException.class,
        () -> validator.validate(book)
    );

    assertThat(exception.getMessage()).isEqualTo(
        "ISBN checksum is invalid");
  }

  @Test
  void validate_should_throw_exception_with_xss_in_title() {
    Book book = TestDataFactory.createTestBook("9785412000046",
        "Test<script>alert('xss')</script>", "", "Premier volet cycle des dieux");

    InvalidReferenceException exception = assertThrows(
        InvalidReferenceException.class,
        () -> validator.validate(book)
    );

    assertThat(exception.getMessage())
        .isEqualTo("Field 'title' contains invalid characters: < > \" ;");
  }

  @Test
  void validate_should_throw_exception_with_xss_in_subtitle() {
    Book book = TestDataFactory.createTestBook("9785412000046", "Nous les dieux",
        "Subtitle with \" dangerous quote", "Premier volet cycle des dieux");

    InvalidReferenceException exception = assertThrows(
        InvalidReferenceException.class,
        () -> validator.validate(book)
    );
    assertThat(exception.getMessage())
        .isEqualTo("Field 'subtitle' contains invalid characters: < > \" ;");
  }

  @Test
  void validate_should_throw_exception_with_xss_in_description() {
    Book book = TestDataFactory.createTestBook("9785412000046", "Nous les dieux", "",
        "Description with <");

    InvalidReferenceException exception = assertThrows(
        InvalidReferenceException.class,
        () -> validator.validate(book)
    );
    assertThat(exception.getMessage())
        .isEqualTo("Field 'description' contains invalid characters: < > \" ;");
  }
}