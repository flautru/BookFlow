package com.bookflow.book_flow.domain.repositories;

import static com.bookflow.book_flow.utils.TestDataFactory.createTestAuthor;
import static org.assertj.core.api.Assertions.assertThat;

import com.bookflow.book_flow.domain.entities.Author;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private AuthorRepository authorRepository;

  @Test
  void save_should_generate_id_and_persist_author() {
    //Give
    Author author = createTestAuthor("Marie", "Duboid", "FRANCE", LocalDate.of(1995, 1, 1));

    //when
    Author savedAuthor = authorRepository.save(author);

    //then
    assertThat(savedAuthor.getId()).isNotNull();
    assertThat(savedAuthor.getFirstName()).isEqualTo("Marie");
    assertThat(savedAuthor.getLastName()).isEqualTo("Duboid");
    assertThat(savedAuthor.getNationality()).isEqualTo("FRANCE");
    assertThat(savedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1995, 1, 1));
  }

  @Test
  void findById_should_return_author_when_exists() {
    //Give
    Author author = createTestAuthor();
    Author savedAuthor = authorRepository.save(author);
    entityManager.flush();

    //when
    Optional<Author> existingAuthor = authorRepository.findById(savedAuthor.getId());

    //then
    assertThat(existingAuthor).isPresent();

    assertThat(existingAuthor.get())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(savedAuthor);
  }

  @Test
  void findById_should_return_empty_when_not_exists() {
    //Give
    Long noExistId = 999L;

    //when
    Optional<Author> optionalAuthor = authorRepository.findById(noExistId);

    //then
    assertThat(optionalAuthor).isEmpty();
  }

}