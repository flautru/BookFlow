package com.bookflow.book_flow.domain.repositories;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bookflow.book_flow.domain.entities.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreRepositoryTest {

  @Autowired
  private GenreRepository genreRepository;

  @Test
  void should_enforce_unique_name_constraint() {
    // Given
    Genre genre1 = new Genre();
    genre1.setName("Science-Fiction");

    Genre genre2 = new Genre();
    genre2.setName("Science-Fiction"); // Même nom !

    // When/Then
    genreRepository.save(genre1);
    assertThrows(DataIntegrityViolationException.class,
        () -> genreRepository.save(genre2));
  }
}