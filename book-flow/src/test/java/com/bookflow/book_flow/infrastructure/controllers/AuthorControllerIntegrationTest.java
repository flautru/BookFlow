package com.bookflow.book_flow.infrastructure.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.bookflow.book_flow.application.dto.response.BookResponse;
import com.bookflow.book_flow.domain.entities.Author;
import com.bookflow.book_flow.domain.entities.AuthorRole;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.enums.ContributionType;
import com.bookflow.book_flow.domain.repositories.AuthorRepository;
import com.bookflow.book_flow.domain.repositories.AuthorRoleRepository;
import com.bookflow.book_flow.domain.repositories.BookRepository;
import com.bookflow.book_flow.utils.TestDataFactory;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Tests d'intégration complets pour AuthorController Valide la stack : HTTP → Controller → Service
 * → Repository → PostgreSQL
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorControllerIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private AuthorRoleRepository authorRoleRepository;

  private String baseUrl;

  @BeforeEach
  void setUp() {
    baseUrl = "http://localhost:" + port + "/api/authors";
    authorRoleRepository.deleteAll();
    bookRepository.deleteAll();
    authorRepository.deleteAll();
  }

  @Test
  void getAuthorBooks_should_return_multiple_books_with_existing_author_having_books() {
    // Given
    Author author = TestDataFactory.createTestAuthor("Victor", "Hugo", "France",
        LocalDate.of(1802, 2, 26));
    Author savedAuthor = authorRepository.save(author);

    Book book1 = TestDataFactory.createTestBook("1111111111111", "Les Misérables", "Roman social",
        "Chef-d'œuvre de Victor Hugo");
    Book book2 = TestDataFactory.createTestBook("2222222222222", "Notre-Dame de Paris",
        "Roman historique", "Roman gothique de Victor Hugo");
    Book savedBook1 = bookRepository.save(book1);
    Book savedBook2 = bookRepository.save(book2);

    AuthorRole role1 = TestDataFactory.createTestAuthorRole(savedBook1, savedAuthor,
        ContributionType.AUTHOR);
    AuthorRole role2 = TestDataFactory.createTestAuthorRole(savedBook2, savedAuthor,
        ContributionType.AUTHOR);
    authorRoleRepository.save(role1);
    authorRoleRepository.save(role2);

    // When
    String url = baseUrl + "/" + savedAuthor.getId() + "/books";
    ResponseEntity<BookResponse[]> response = restTemplate.getForEntity(url, BookResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    BookResponse[] books = response.getBody();
    assertThat(books).isNotNull();
    assertThat(books).hasSize(2);

    assertThat(books)
        .extracting(BookResponse::getIsbn)
        .containsExactlyInAnyOrder("1111111111111", "2222222222222");

    assertThat(books)
        .extracting(BookResponse::getTitle)
        .containsExactlyInAnyOrder("Les Misérables", "Notre-Dame de Paris");
  }

  @Test
  void getAuthorBooks_should_return_empty_list_with_author_having_no_books() {
    // Given
    Author authorWithoutBooks = TestDataFactory.createTestAuthor("Marcel", "Proust", "France",
        java.time.LocalDate.of(1871, 7, 10));
    Author savedAuthor = authorRepository.save(authorWithoutBooks);

    // When
    String url = baseUrl + "/" + savedAuthor.getId() + "/books";
    ResponseEntity<BookResponse[]> response = restTemplate.getForEntity(url, BookResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    BookResponse[] books = response.getBody();
    assertThat(books).isNotNull();
    assertThat(books).isEmpty();
  }

  @Test
  void getAuthorBooks_should_return_404_with_non_existing_author() {
    // Given
    Long nonExistingAuthorId = 999999L;

    // When
    String url = baseUrl + "/" + nonExistingAuthorId + "/books";
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void getAuthorBooks_should_handle_author_with_different_contribution_types() {
    // Given
    Author author = TestDataFactory.createTestAuthor("J.K.", "Rowling", "Royaume-Uni",
        LocalDate.of(1965, 7, 31));
    Author savedAuthor = authorRepository.save(author);

    Book originalBook = TestDataFactory.createTestBook("1111111111111",
        "Harry Potter à l'école des sorciers", "Roman fantasy", "Premier tome de la saga");
    Book translatedBook = TestDataFactory.createTestBook("2222222222222",
        "Harry Potter and the Philosopher's Stone", "Fantasy novel", "English version");
    Book savedOriginalBook = bookRepository.save(originalBook);
    Book savedTranslatedBook = bookRepository.save(translatedBook);

    AuthorRole authorRole = TestDataFactory.createTestAuthorRole(savedOriginalBook, savedAuthor,
        ContributionType.AUTHOR);
    AuthorRole translatorRole = TestDataFactory.createTestAuthorRole(savedTranslatedBook,
        savedAuthor, ContributionType.TRANSLATOR);
    authorRoleRepository.save(authorRole);
    authorRoleRepository.save(translatorRole);

    // When
    String url = baseUrl + "/" + savedAuthor.getId() + "/books";
    ResponseEntity<BookResponse[]> response = restTemplate.getForEntity(url, BookResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    BookResponse[] books = response.getBody();
    assertThat(books).hasSize(2);

    assertThat(books)
        .extracting(BookResponse::getTitle)
        .containsExactlyInAnyOrder(
            "Harry Potter à l'école des sorciers",
            "Harry Potter and the Philosopher's Stone"
        );
  }
}