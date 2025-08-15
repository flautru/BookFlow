package com.bookflow.book_flow.domain.entities;

import com.bookflow.book_flow.domain.enums.BookCondition;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 13, unique = true, nullable = false)
  private String isbn;

  @Column(length = 255, nullable = false)
  private String title;

  @Column(length = 255)
  private String subtitle;

  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<PhysicalBook> physicalBooks = new HashSet<>();

  public long getAvailableCopies() {
    return physicalBooks.stream()
        .filter(pb -> pb.getCondition() != BookCondition.LOST)
        .count();
  }
}
