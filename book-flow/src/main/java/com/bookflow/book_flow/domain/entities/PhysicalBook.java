package com.bookflow.book_flow.domain.entities;

import com.bookflow.book_flow.domain.enums.BookCondition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "physical_books")
@Data
public class PhysicalBook {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  @Column(unique = true, nullable = false)
  private String barcode;

  @Enumerated(EnumType.STRING)
  private BookCondition condition;

  // Localisation physique - String simple pour l'instant
  private String location; // "Salle A, Étagère 3, Rangée 2"

  private LocalDate acquisitionDate;
}
