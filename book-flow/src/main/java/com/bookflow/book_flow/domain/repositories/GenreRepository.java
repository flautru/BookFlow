package com.bookflow.book_flow.domain.repositories;

import com.bookflow.book_flow.domain.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
