package com.bookflow.book_flow.domain.entities;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.bookflow.book_flow.domain.enums.ContributionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "author_roles",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"book_id", "author_id", "contribution_type"}))
@Data
@ToString(exclude = {"book", "author"})
@EqualsAndHashCode(exclude = {"book", "author"})
public class AuthorRole {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false)
  private Book book;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(nullable = false)
  private Author author;

  @Enumerated(EnumType.STRING)
  private ContributionType contributionType;
}
