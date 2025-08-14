package com.bookflow.book_flow.domain.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.bookflow.book_flow.domain.entities.User;
import com.bookflow.book_flow.domain.enums.UserType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  void save_should_generate_id_and_persist_user() {
    //Give
    User user = createTestUser();

    //when
    User savedUser = userRepository.save(user);

    //then
    assertThat(savedUser.getId()).isNotNull();
    assertThat(savedUser.getEmail()).isEqualTo("marie@dubois.com");
    assertThat(savedUser.getUserType()).isEqualTo(UserType.STUDENT);

    assertThat(savedUser)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(user);
  }

  @Test
  void findByMemberNumber_should_return_user_when_exists() {
    //Give
    Long memberNumber = 14587L;
    User user = createTestUser();
    User savedUser = userRepository.save(user);
    entityManager.flush();

    //when
    Optional<User> existingUser = userRepository.findByMemberNumber(memberNumber);

    //then
    assertThat(existingUser.get().getId()).isNotNull();

    assertThat(existingUser.get())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(savedUser);
  }

  @Test
  void findByMemberNumber_should_return_empty_when_not_exists() {
    //Give
    Long noExistMember = 999L;

    //when
    Optional<User> optionalUser = userRepository.findByMemberNumber(noExistMember);

    //then
    assertThat(optionalUser).isEmpty();
  }

  @Test
  void findByUserType_should_return_list_user_exists() {
    //Give
    User user1 = createTestUser();
    User user2 = createTestUser("Jean", "Dupont", "1 rue test", "jean@dupont", UserType.STUDENT,
        123L);
    User user3 = createTestUser("Jeanne", "Dupond", "12 rue test", "jeanne@dupond",
        UserType.CLASSIC, 312L);

    List<User> users = userRepository.saveAll(List.of(user1, user2, user3));

    //when
    List<User> studentsUsers = userRepository.findAllByUserType(UserType.STUDENT);

    //then
    assertThat(studentsUsers).isNotNull().hasSize(2)
        .extracting(User::getUserType).containsExactly(UserType.STUDENT, UserType.STUDENT);

    assertThat(studentsUsers).extracting(User::getFirstName)
        .containsExactly("Marie", "Jean");

    assertThat(studentsUsers).extracting(User::getEmail)
        .containsExactly("marie@dubois.com", "jean@dupont");

  }

  private User createTestUser() {
    User user = new User();
    user.setFirstName("Marie");
    user.setLastName("Dubois");
    user.setAddress("12 rue Test");
    user.setEmail("marie@dubois.com");
    user.setUserType(UserType.STUDENT);
    user.setMemberNumber(14587L);
    return user;
  }

  private User createTestUser(String firstName, String lastName, String adress, String email,
      UserType userType, Long numberMember) {
    User user = new User();
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setAddress(adress);
    user.setEmail(email);
    user.setUserType(userType);
    user.setMemberNumber(numberMember);
    return user;
  }

  private User createTestUserWithBirthDay(LocalDate birthDate) {
    User user = createTestUser();
    user.setBirthDate(birthDate);

    return user;
  }
}