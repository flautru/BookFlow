package com.bookflow.book_flow.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.bookflow.book_flow.domain.enums.UserType;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void constructor_should_create_user_with_all_fields() {
    // Given
    User user = createTestUser("Jean", "Dupont", "1 avenue du test", "jean@dupont.com",
        UserType.STUDENT);
    // When/Then
    assertThat(user.getFirstName()).isEqualTo("Jean");
    assertThat(user.getLastName()).isEqualTo("Dupont");
    assertThat(user.getEmail()).isEqualTo("jean@dupont.com");
    assertThat(user.getAddress()).isEqualTo("1 avenue du test");
    assertThat(user.getUserType()).isEqualTo(UserType.STUDENT);
  }

  @Test
  void isAdult_should_return_true() {
    // Given
    User user = createTestUserWithBirthDay(LocalDate.of(1995, 8, 14));

    // When/Then
    assertThat(user.isAdult()).isEqualTo(true);

  }

  @Test
  void isAdult_should_return_false() {
    // Given
    User user = createTestUserWithBirthDay(LocalDate.of(2025, 1, 1));

    // When/Then
    assertThat(user.isAdult()).isEqualTo(false);
  }

  private User createTestUser() {
    User user = new User();
    user.setFirstName("Marie");
    user.setLastName("Dubois");
    user.setAddress("12 rue Test");
    user.setEmail("marie@dubois.com");
    user.setUserType(UserType.STUDENT);
    return user;
  }

  private User createTestUser(String firstName, String lastName, String adress, String email,
      UserType userType) {
    User user = new User();
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setAddress(adress);
    user.setEmail(email);
    user.setUserType(userType);
    return user;
  }

  private User createTestUserWithBirthDay(LocalDate birthDate) {
    User user = createTestUser();
    user.setBirthDate(birthDate);

    return user;
  }

  @Test
  void isAdult_should_handle_exactly_18_years() {
    // Given
    LocalDate edgeYears = LocalDate.now().minusYears(18);
    User user = createTestUserWithBirthDay(edgeYears);

    // When/Then
    assertThat(user.isAdult()).isEqualTo(true);
  }
}