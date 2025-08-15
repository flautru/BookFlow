package com.bookflow.book_flow.application.services;

import com.bookflow.book_flow.application.exceptions.EntityNotFoundException;
import com.bookflow.book_flow.domain.entities.Author;
import com.bookflow.book_flow.domain.repositories.AuthorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository authorRepository;

  // Core Author operations
  public Author findById(Long id) {
    return authorRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Author", id));
  }

  //TODO : CHANGE WITH PAGINATION
  public List<Author> findAll() {
    return authorRepository.findAll();
  }

  public Author save(Author author) {
    Author savedAuthor = authorRepository.save(author);

    return savedAuthor;
  }

  public List<Author> findByNationality(String nationality) {
    return authorRepository.findByNationality(nationality);
  }
}
