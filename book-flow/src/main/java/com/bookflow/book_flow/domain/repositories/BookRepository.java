package com.bookflow.book_flow.domain.repositories;

import com.bookflow.book_flow.domain.entities.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

  Optional<Book> findByIsbn(String isbn);

  List<Book> findByTitleContainingIgnoreCase(String title);

}
