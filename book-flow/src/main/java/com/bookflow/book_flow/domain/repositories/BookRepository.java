package com.bookflow.book_flow.domain.repositories;

import com.bookflow.book_flow.domain.entities.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

  Optional<Book> findByIsbn(String isbn);

  List<Book> findByTitleContainingIgnoreCase(String title);

  @Query("SELECT DISTINCT b FROM Book b " +
      "LEFT JOIN FETCH b.authorRoles ar " +
      "LEFT JOIN FETCH ar.author " +
      "LEFT JOIN FETCH b.bookGenres bg " +
      "LEFT JOIN FETCH bg.genre")
  List<Book> findAllWithRelations();
}
