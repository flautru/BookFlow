package com.bookflow.book_flow.domain.repositories;

import com.bookflow.book_flow.domain.entities.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

  List<Author> findByNationality(String nationality);
}
