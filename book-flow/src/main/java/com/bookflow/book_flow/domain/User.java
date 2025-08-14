package com.bookflow.book_flow.domain;

import com.bookflow.book_flow.domain.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.Period;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private Long memberNumber;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;

  private LocalDate birthDate;
  private String address;
  @Enumerated(EnumType.STRING)
  private UserType userType;

  //TODO : Modifier le 18 et extraire la valeur pour avoir un age mineur configurable en fonction du pays
  public boolean isAdult() {
    return Period.between(getBirthDate(), LocalDate.now()).getYears() >= 18;
  }

}
