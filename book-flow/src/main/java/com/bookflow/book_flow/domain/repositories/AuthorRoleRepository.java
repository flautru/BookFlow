package com.bookflow.book_flow.domain.repositories;

import com.bookflow.book_flow.domain.entities.Author;
import com.bookflow.book_flow.domain.entities.AuthorRole;
import com.bookflow.book_flow.domain.entities.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRoleRepository extends JpaRepository<AuthorRole, Long> {

  @Query("SELECT ar.book FROM AuthorRole ar WHERE ar.author = :author")
  List<Book> findBooksByAuthor(@Param("author") Author author);

  List<AuthorRole> findByBook(@Param("book") Book book);
}
