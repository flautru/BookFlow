package com.bookflow.book_flow.domain.repositories;

import com.bookflow.book_flow.domain.entities.PhysicalBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalBookRepository extends JpaRepository<PhysicalBook, Long> {

}
