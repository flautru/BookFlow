package com.bookflow.book_flow.domain.entities;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.bookflow.book_flow.domain.enums.GenreIntensity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "book_genres")
@Data
public class BookGenre {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false)
  private Book book;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false)
  private Genre genre;

  @Enumerated(EnumType.STRING)
  private GenreIntensity intensity;
}
