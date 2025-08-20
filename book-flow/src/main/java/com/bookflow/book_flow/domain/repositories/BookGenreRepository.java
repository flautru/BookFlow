package com.bookflow.book_flow.domain.repositories;

import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.entities.BookGenre;
import com.bookflow.book_flow.domain.entities.Genre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {

  @Query("SELECT bg.book FROM BookGenre bg WHERE bg.genre = :genre")
  List<Book> findBooksByGenre(@Param("genre") Genre genre);

  List<BookGenre> findByBook(@Param("book") Book book);

}
