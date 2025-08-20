package com.bookflow.book_flow.domain.repositories;

import static com.bookflow.book_flow.domain.enums.GenreIntensity.PRIMARY;
import static com.bookflow.book_flow.domain.enums.GenreIntensity.SECONDARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.entities.BookGenre;
import com.bookflow.book_flow.domain.entities.Genre;
import com.bookflow.book_flow.utils.TestDataFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class BookGenreRepositoryTest {

  @Autowired
  private BookGenreRepository bookGenreRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  void findBooksByGenre_should_return_multiple_books_with_genre_having_several_books() {
    // Given
    Genre actionGenre = TestDataFactory.createTestGenre("Action", "Action and adventure genre");
    Book book1 = TestDataFactory.createTestBook("1111111111111", "Action Book 1", "Subtitle 1",
        "First action book");
    Book book2 = TestDataFactory.createTestBook("2222222222222", "Action Book 2", "Subtitle 2",
        "Second action book");

    genreRepository.save(actionGenre);
    bookRepository.save(book1);
    bookRepository.save(book2);

    BookGenre bookGenre1 = TestDataFactory.createTestBookGenre(book1, actionGenre, PRIMARY);
    BookGenre bookGenre2 = TestDataFactory.createTestBookGenre(book2, actionGenre, SECONDARY);
    bookGenreRepository.save(bookGenre1);
    bookGenreRepository.save(bookGenre2);

    // When
    List<Book> books = bookGenreRepository.findBooksByGenre(actionGenre);

    // Then
    assertThat(books).hasSize(2);
    assertThat(books).extracting(Book::getIsbn)
        .containsExactlyInAnyOrder("1111111111111", "2222222222222");
  }

  @Test
  void findBooksByGenre_should_return_empty_list_with_genre_having_no_books() {
    // Given
    Genre unusedGenre = TestDataFactory.createTestGenre("Horror", "Horror genre without books");
    genreRepository.save(unusedGenre);

    // When
    List<Book> books = bookGenreRepository.findBooksByGenre(unusedGenre);

    // Then
    assertThat(books).isEmpty();
  }

  @Test
  void findGenresByBook_should_return_multiple_genres_with_book_having_several_() {
    // Given
    Book book = TestDataFactory.createTestBook();
    Genre actionGenre = TestDataFactory.createTestGenre("Action", "Action genre");
    Genre adventureGenre = TestDataFactory.createTestGenre("Adventure", "Adventure genre");
    Genre romanceGenre = TestDataFactory.createTestGenre("Romance", "Romance genre");

    bookRepository.save(book);
    genreRepository.save(actionGenre);
    genreRepository.save(adventureGenre);
    genreRepository.save(romanceGenre);

    BookGenre actionPrimary = TestDataFactory.createTestBookGenre(book, actionGenre, PRIMARY);
    BookGenre adventurePrimary = TestDataFactory.createTestBookGenre(book, adventureGenre, PRIMARY);
    BookGenre romanceSecondary = TestDataFactory.createTestBookGenre(book, romanceGenre, SECONDARY);
    bookGenreRepository.save(actionPrimary);
    bookGenreRepository.save(adventurePrimary);
    bookGenreRepository.save(romanceSecondary);

    // When
    List<BookGenre> bookGenres = bookGenreRepository.findByBook(book);

    // Then
    assertThat(bookGenres).hasSize(3);
    assertThat(bookGenres).extracting(BookGenre::getGenre).extracting(Genre::getName)
        .containsExactlyInAnyOrder("Action", "Adventure", "Romance");
  }

  @Test
  void findGenresByBook_should_return_empty_list_with_book_having_no_() {
    // Given
    Book bookWithoutGenres = TestDataFactory.createTestBook("9999999999999", "Unclassified Book",
        "No Genre", "A book without any genre");
    bookRepository.save(bookWithoutGenres);

    // When
    List<BookGenre> bookGenres = bookGenreRepository.findByBook(bookWithoutGenres);

    // Then
    assertThat(bookGenres).isEmpty();
  }

  @Test
  void save_should_allow_multiple_primary_genres_with_same_book_different_genres() {
    // Given
    Book book = TestDataFactory.createTestBook();
    Genre actionGenre = TestDataFactory.createTestGenre("Action", "Action genre");
    Genre adventureGenre = TestDataFactory.createTestGenre("Adventure", "Adventure genre");
    Genre sciFiGenre = TestDataFactory.createTestGenre("Science Fiction", "Science Fiction genre");

    bookRepository.save(book);
    genreRepository.save(actionGenre);
    genreRepository.save(adventureGenre);
    genreRepository.save(sciFiGenre);

    BookGenre actionPrimary = TestDataFactory.createTestBookGenre(book, actionGenre, PRIMARY);
    BookGenre adventurePrimary = TestDataFactory.createTestBookGenre(book, adventureGenre, PRIMARY);
    BookGenre sciFiPrimary = TestDataFactory.createTestBookGenre(book, sciFiGenre, PRIMARY);

    // When & Then
    assertThatNoException().isThrownBy(() -> {
      bookGenreRepository.save(actionPrimary);
      bookGenreRepository.save(adventurePrimary);
      bookGenreRepository.save(sciFiPrimary);
    });

    List<BookGenre> savedBookGenres = bookGenreRepository.findAll();
    assertThat(savedBookGenres).hasSize(3);
    assertThat(savedBookGenres).allMatch(bg -> bg.getIntensity() == PRIMARY);
  }

  @Test
  void save_should_allow_same_genre_different_intensity_with_different_books() {
    // Given
    Genre actionGenre = TestDataFactory.createTestGenre("Action", "Action genre");
    Book book1 = TestDataFactory.createTestBook("1111111111111", "Pure Action", "Subtitle 1",
        "Pure action book");
    Book book2 = TestDataFactory.createTestBook("2222222222222", "Romance with Action",
        "Subtitle 2", "Romance book with some action");

    genreRepository.save(actionGenre);
    bookRepository.save(book1);
    bookRepository.save(book2);

    BookGenre actionPrimaryForBook1 = TestDataFactory.createTestBookGenre(book1, actionGenre,
        PRIMARY);
    BookGenre actionSecondaryForBook2 = TestDataFactory.createTestBookGenre(book2, actionGenre,
        SECONDARY);

    // When & Then
    assertThatNoException().isThrownBy(() -> {
      bookGenreRepository.save(actionPrimaryForBook1);
      bookGenreRepository.save(actionSecondaryForBook2);
    });

    // Verify both are saved with different intensities
    List<BookGenre> savedBookGenres = bookGenreRepository.findAll();
    assertThat(savedBookGenres).hasSize(2);
    assertThat(savedBookGenres).extracting(BookGenre::getIntensity)
        .containsExactlyInAnyOrder(PRIMARY, SECONDARY);
  }

}