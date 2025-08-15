package com.bookflow.book_flow.domain.repositories;

import static com.bookflow.book_flow.domain.enums.ContributionType.AUTHOR;
import static com.bookflow.book_flow.domain.enums.ContributionType.CO_AUTHOR;
import static com.bookflow.book_flow.domain.enums.ContributionType.TRANSLATOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.bookflow.book_flow.domain.entities.Author;
import com.bookflow.book_flow.domain.entities.AuthorRole;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.utils.TestDataFactory;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class AuthorRoleRepositoryTest {

  @Autowired
  private AuthorRoleRepository authorRoleRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private AuthorRepository authorRepository;

  @Test
  void findBooksByAuthor_should_return_multiple_books_with_author_having_several_books() {
    // Given
    Author author = TestDataFactory.createTestAuthor();
    Book book1 = TestDataFactory.createTestBook("1111111111111", "Book 1", "Subtitle 1",
        "Description 1");
    Book book2 = TestDataFactory.createTestBook("2222222222222", "Book 2", "Subtitle 2",
        "Description 2");

    authorRepository.save(author);
    bookRepository.save(book1);
    bookRepository.save(book2);

    AuthorRole role1 = TestDataFactory.createTestAuthorRole(book1, author, AUTHOR);
    AuthorRole role2 = TestDataFactory.createTestAuthorRole(book2, author, TRANSLATOR);
    authorRoleRepository.save(role1);
    authorRoleRepository.save(role2);

    // When
    List<Book> books = authorRoleRepository.findBooksByAuthor(author);

    // Then
    assertThat(books).hasSize(2);
    assertThat(books).extracting(Book::getIsbn)
        .containsExactlyInAnyOrder("1111111111111", "2222222222222");
  }

  @Test
  void findBooksByAuthor_should_return_empty_list_with_author_having_no_books() {
    // Given
    Author authorWithoutBooks = TestDataFactory.createTestAuthor("John", "Doe", "USA",
        LocalDate.of(1980, 5, 15));
    authorRepository.save(authorWithoutBooks);

    // When
    List<Book> books = authorRoleRepository.findBooksByAuthor(authorWithoutBooks);

    // Then
    assertThat(books).isEmpty();
  }

  @Test
  void findAuthorsByBook_should_return_multiple_authors_with_book_having_several_authors() {
    // Given
    Book book = TestDataFactory.createTestBook();
    Author author1 = TestDataFactory.createTestAuthor("Victor", "Hugo", "France",
        LocalDate.of(1802, 2, 26));
    Author author2 = TestDataFactory.createTestAuthor("Alexandre", "Dumas", "France",
        LocalDate.of(1802, 7, 24));

    bookRepository.save(book);
    authorRepository.save(author1);
    authorRepository.save(author2);

    AuthorRole role1 = TestDataFactory.createTestAuthorRole(book, author1, AUTHOR);
    AuthorRole role2 = TestDataFactory.createTestAuthorRole(book, author2, CO_AUTHOR);
    authorRoleRepository.save(role1);
    authorRoleRepository.save(role2);

    // When
    List<Author> authors = authorRoleRepository.findAuthorsByBook(book);

    // Then
    assertThat(authors).hasSize(2);
    assertThat(authors).extracting(Author::getLastName).containsExactlyInAnyOrder("Hugo", "Dumas");
  }

  @Test
  void findAuthorsByBook_should_return_empty_list_with_book_having_no_authors() {
    // Given
    Book bookWithoutAuthors = TestDataFactory.createTestBook("9999999999999", "Orphan Book",
        "No Authors", "A book without authors");
    bookRepository.save(bookWithoutAuthors);

    // When
    List<Author> authors = authorRoleRepository.findAuthorsByBook(bookWithoutAuthors);

    // Then
    assertThat(authors).isEmpty();
  }

  @Test
  void save_should_allow_same_author_with_different_roles_with_same_book_and_author() {
    // Given
    Book book = TestDataFactory.createTestBook();
    Author author = TestDataFactory.createTestAuthor();

    bookRepository.save(book);
    authorRepository.save(author);

    AuthorRole roleAuthor = TestDataFactory.createTestAuthorRole(book, author, AUTHOR);
    AuthorRole roleTranslator = TestDataFactory.createTestAuthorRole(book, author, TRANSLATOR);

    // When & Then
    assertThatNoException().isThrownBy(() -> {
      authorRoleRepository.save(roleAuthor);
      authorRoleRepository.save(roleTranslator);
    });

    List<AuthorRole> savedRoles = authorRoleRepository.findAll();
    assertThat(savedRoles).hasSize(2);
  }
}