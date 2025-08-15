package com.bookflow.book_flow.infrastructure.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.bookflow.book_flow.application.dto.request.BookRequest;
import com.bookflow.book_flow.application.dto.response.AuthorResponse;
import com.bookflow.book_flow.application.dto.response.BookResponse;
import com.bookflow.book_flow.application.dto.response.GenreResponse;
import com.bookflow.book_flow.domain.entities.Author;
import com.bookflow.book_flow.domain.entities.AuthorRole;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.entities.BookGenre;
import com.bookflow.book_flow.domain.entities.Genre;
import com.bookflow.book_flow.domain.enums.ContributionType;
import com.bookflow.book_flow.domain.enums.GenreIntensity;
import com.bookflow.book_flow.domain.repositories.AuthorRepository;
import com.bookflow.book_flow.domain.repositories.AuthorRoleRepository;
import com.bookflow.book_flow.domain.repositories.BookGenreRepository;
import com.bookflow.book_flow.domain.repositories.BookRepository;
import com.bookflow.book_flow.domain.repositories.GenreRepository;
import com.bookflow.book_flow.utils.TestDataFactory;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookControllerIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private AuthorRoleRepository authorRoleRepository;

  @Autowired
  private BookGenreRepository bookGenreRepository;

  private String baseUrl;

  @BeforeEach
  void setUp() {
    baseUrl = "http://localhost:" + port + "/api/books";
    authorRoleRepository.deleteAll();
    bookGenreRepository.deleteAll();
    bookRepository.deleteAll();
    authorRepository.deleteAll();
    genreRepository.deleteAll();
  }

  @Test
  void getAllBooks_should_return_all_books_with_existing_books() {
    // Given - Créer plusieurs livres
    Book book1 = TestDataFactory.createTestBook("1111111111111", "Le Petit Prince",
        "Conte philosophique", "Antoine de Saint-Exupéry");
    Book book2 = TestDataFactory.createTestBook("2222222222222", "1984", "Roman dystopique",
        "George Orwell");
    Book book3 = TestDataFactory.createTestBook("3333333333333", "L'Étranger",
        "Roman philosophique", "Albert Camus");

    bookRepository.save(book1);
    bookRepository.save(book2);
    bookRepository.save(book3);

    // When
    ResponseEntity<BookResponse[]> response = restTemplate.getForEntity(baseUrl,
        BookResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    BookResponse[] books = response.getBody();
    assertThat(books).isNotNull();
    assertThat(books).hasSize(3);

    assertThat(books)
        .extracting(BookResponse::getIsbn)
        .containsExactlyInAnyOrder("1111111111111", "2222222222222", "3333333333333");

    assertThat(books)
        .extracting(BookResponse::getTitle)
        .containsExactlyInAnyOrder("Le Petit Prince", "1984", "L'Étranger");
  }

  @Test
  void getAllBooks_should_return_empty_list_with_no_books() {
    // Given

    // When
    ResponseEntity<BookResponse[]> response = restTemplate.getForEntity(baseUrl,
        BookResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    BookResponse[] books = response.getBody();
    assertThat(books).isNotNull();
    assertThat(books).isEmpty();
  }

  @Test
  void getBookByIsbn_should_return_book_with_existing_isbn() {
    // Given
    Book book = TestDataFactory.createTestBook("1234567890123", "Test Book", "Test Subtitle",
        "Test Description");
    bookRepository.save(book);

    // When
    String url = baseUrl + "/1234567890123";
    ResponseEntity<BookResponse> response = restTemplate.getForEntity(url, BookResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    BookResponse bookResponse = response.getBody();
    assertThat(bookResponse).isNotNull();
    assertThat(bookResponse.getIsbn()).isEqualTo("1234567890123");
    assertThat(bookResponse.getTitle()).isEqualTo("Test Book");
    assertThat(bookResponse.getSubtitle()).isEqualTo("Test Subtitle");
    assertThat(bookResponse.getDescription()).isEqualTo("Test Description");
  }

  @Test
  void getBookByIsbn_should_return_404_with_non_existing_isbn() {
    // Given
    String nonExistingIsbn = "9999999999999";

    // When
    String url = baseUrl + "/" + nonExistingIsbn;
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void searchBooksByTitle_should_return_books_with_matching_title() {
    // Given
    Book book1 = TestDataFactory.createTestBook("1111111111111",
        "Harry Potter à l'école des sorciers", "Tome 1", "Premier tome");
    Book book2 = TestDataFactory.createTestBook("2222222222222",
        "Harry Potter et la chambre des secrets", "Tome 2", "Deuxième tome");
    Book book3 = TestDataFactory.createTestBook("3333333333333", "Le Seigneur des Anneaux",
        "Fantasy", "Tolkien");

    bookRepository.save(book1);
    bookRepository.save(book2);
    bookRepository.save(book3);

    // When
    String url = baseUrl + "/search?title=Harry Potter";
    ResponseEntity<BookResponse[]> response = restTemplate.getForEntity(url, BookResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    BookResponse[] books = response.getBody();
    assertThat(books).isNotNull();
    assertThat(books).hasSize(2);

    assertThat(books)
        .extracting(BookResponse::getTitle)
        .allMatch(title -> title.contains("Harry Potter"));
  }

  @Test
  void searchBooksByTitle_should_return_empty_list_with_no_matching_title() {
    // Given
    Book book = TestDataFactory.createTestBook("1111111111111", "Le Petit Prince", "Conte",
        "Saint-Exupéry");
    bookRepository.save(book);

    // When
    String url = baseUrl + "/search?title=NonExistentTitle";
    ResponseEntity<BookResponse[]> response = restTemplate.getForEntity(url, BookResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    BookResponse[] books = response.getBody();
    assertThat(books).isNotNull();
    assertThat(books).isEmpty();
  }

  @Test
  void getBooksByAuthor_should_return_books_with_existing_author() {
    // Given
    Author author = TestDataFactory.createTestAuthor("Agatha", "Christie", "Royaume-Uni",
        LocalDate.of(1890, 9, 15));
    Author savedAuthor = authorRepository.save(author);

    Book book1 = TestDataFactory.createTestBook("1111111111111", "Le Crime de l'Orient-Express",
        "Polar", "Hercule Poirot");
    Book book2 = TestDataFactory.createTestBook("2222222222222", "Dix Petits Nègres", "Thriller",
        "Roman policier");
    Book savedBook1 = bookRepository.save(book1);
    Book savedBook2 = bookRepository.save(book2);

    AuthorRole role1 = TestDataFactory.createTestAuthorRole(savedBook1, savedAuthor,
        ContributionType.AUTHOR);
    AuthorRole role2 = TestDataFactory.createTestAuthorRole(savedBook2, savedAuthor,
        ContributionType.AUTHOR);
    authorRoleRepository.save(role1);
    authorRoleRepository.save(role2);

    // When
    String url = baseUrl + "/by-author/" + savedAuthor.getId();
    ResponseEntity<BookResponse[]> response = restTemplate.getForEntity(url, BookResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    BookResponse[] books = response.getBody();
    assertThat(books).isNotNull();
    assertThat(books).hasSize(2);

    assertThat(books)
        .extracting(BookResponse::getTitle)
        .containsExactlyInAnyOrder("Le Crime de l'Orient-Express", "Dix Petits Nègres");
  }

  @Test
  void getBooksByAuthor_should_return_404_with_non_existing_author() {
    // Given
    Long nonExistingAuthorId = 999999L;

    // When
    String url = baseUrl + "/by-author/" + nonExistingAuthorId;
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

  }

  @Test
  void getBookAuthors_should_return_authors_with_existing_book() {
    // Given

    Book book = TestDataFactory.createTestBook("1111111111111", "Livre collaboratif",
        "Plusieurs auteurs", "Écrit à plusieurs mains");
    Book savedBook = bookRepository.save(book);

    Author author1 = TestDataFactory.createTestAuthor("Jean", "Dupont", "France",
        LocalDate.of(1970, 1, 1));
    Author author2 = TestDataFactory.createTestAuthor("Marie", "Martin", "France",
        LocalDate.of(1975, 5, 5));
    Author savedAuthor1 = authorRepository.save(author1);
    Author savedAuthor2 = authorRepository.save(author2);

    AuthorRole role1 = TestDataFactory.createTestAuthorRole(savedBook, savedAuthor1,
        ContributionType.AUTHOR);
    AuthorRole role2 = TestDataFactory.createTestAuthorRole(savedBook, savedAuthor2,
        ContributionType.CO_AUTHOR);
    authorRoleRepository.save(role1);
    authorRoleRepository.save(role2);

    // When
    String url = baseUrl + "/" + savedBook.getId() + "/authors";
    ResponseEntity<AuthorResponse[]> response = restTemplate.getForEntity(url,
        AuthorResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    AuthorResponse[] authors = response.getBody();
    assertThat(authors).isNotNull();
    assertThat(authors).hasSize(2);

    assertThat(authors)
        .extracting(AuthorResponse::lastName)
        .containsExactlyInAnyOrder("Dupont", "Martin");
  }

  @Test
  void getBookAuthors_should_return_404_with_non_existing_book() {
    // Given
    Long nonExistingBookId = 999999L;

    // When
    String url = baseUrl + "/" + nonExistingBookId + "/authors";
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void getBookGenres_should_return_genres_with_existing_book() {
    // Given
    Book book = TestDataFactory.createTestBook("1111111111111", "Livre multi-genre",
        "Action et Romance", "Mélange de genres");
    Book savedBook = bookRepository.save(book);

    Genre actionGenre = TestDataFactory.createTestGenre("Action", "Films et livres d'action");
    Genre romanceGenre = TestDataFactory.createTestGenre("Romance", "Histoires d'amour");
    Genre adventureGenre = TestDataFactory.createTestGenre("Adventure", "Aventures épiques");
    Genre savedActionGenre = genreRepository.save(actionGenre);
    Genre savedRomanceGenre = genreRepository.save(romanceGenre);
    Genre savedAdventureGenre = genreRepository.save(adventureGenre);

    BookGenre bookGenre1 = TestDataFactory.createTestBookGenre(savedBook, savedActionGenre,
        GenreIntensity.PRIMARY);
    BookGenre bookGenre2 = TestDataFactory.createTestBookGenre(savedBook, savedRomanceGenre,
        GenreIntensity.PRIMARY);
    BookGenre bookGenre3 = TestDataFactory.createTestBookGenre(savedBook, savedAdventureGenre,
        GenreIntensity.SECONDARY);
    bookGenreRepository.save(bookGenre1);
    bookGenreRepository.save(bookGenre2);
    bookGenreRepository.save(bookGenre3);

    // When
    String url = baseUrl + "/" + savedBook.getId() + "/genres";
    ResponseEntity<GenreResponse[]> response = restTemplate.getForEntity(url,
        GenreResponse[].class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    GenreResponse[] genres = response.getBody();
    assertThat(genres).isNotNull();
    assertThat(genres).hasSize(3);

    assertThat(genres)
        .extracting(GenreResponse::name)
        .containsExactlyInAnyOrder("Action", "Romance", "Adventure");
  }

  @Test
  void getBookGenres_should_return_404_with_non_existing_book() {
    // Given
    Long nonExistingBookId = 999999L;

    // When
    String url = baseUrl + "/" + nonExistingBookId + "/genres";
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void createBook_should_create_book_with_valid_request() {
    // Given - Requête valide
    BookRequest request = new BookRequest();
    request.setIsbn("1234567890123");
    request.setTitle("Nouveau Livre");
    request.setSubtitle("Sous-titre test");
    request.setDescription("Description du nouveau livre");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<BookRequest> entity = new HttpEntity<>(request, headers);

    // When
    ResponseEntity<BookResponse> response = restTemplate.postForEntity(baseUrl, entity,
        BookResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    BookResponse createdBook = response.getBody();
    assertThat(createdBook).isNotNull();
    assertThat(createdBook.getIsbn()).isEqualTo("1234567890123");
    assertThat(createdBook.getTitle()).isEqualTo("Nouveau Livre");
    assertThat(createdBook.getId()).isNotNull(); // ID généré

    assertThat(bookRepository.count()).isEqualTo(1);
    Book savedBook = bookRepository.findByIsbn("1234567890123").orElse(null);
    assertThat(savedBook).isNotNull();
    assertThat(savedBook.getTitle()).isEqualTo("Nouveau Livre");
  }

  @Test
  void createBook_should_return_400_with_invalid_request() {
    // Given
    BookRequest invalidRequest = new BookRequest();
    invalidRequest.setTitle("Livre sans ISBN");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<BookRequest> entity = new HttpEntity<>(invalidRequest, headers);

    // When
    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, entity, String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    assertThat(bookRepository.count()).isEqualTo(0);
  }

  @Test
  void createBook_should_return_409_with_duplicate_isbn() {
    // Given
    Book existingBook = TestDataFactory.createTestBook("1234567890123", "Livre existant",
        "Déjà en base", "Premier livre");
    bookRepository.save(existingBook);

    BookRequest duplicateRequest = new BookRequest();
    duplicateRequest.setIsbn("1234567890123"); // ISBN déjà utilisé
    duplicateRequest.setTitle("Livre dupliqué");
    duplicateRequest.setSubtitle("Même ISBN");
    duplicateRequest.setDescription("Ne devrait pas être créé");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<BookRequest> entity = new HttpEntity<>(duplicateRequest, headers);

    // When
    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, entity, String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

    assertThat(bookRepository.count()).isEqualTo(1);
    Book onlyBook = bookRepository.findByIsbn("1234567890123").orElse(null);
    assertThat(onlyBook.getTitle()).isEqualTo("Livre existant"); // Pas le dupliqué
  }
}