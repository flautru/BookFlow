package com.bookflow.book_flow.infrastructure.controllers;

import com.bookflow.book_flow.application.dto.response.BookResponse;
import com.bookflow.book_flow.application.mappers.AuthorMapper;
import com.bookflow.book_flow.application.mappers.BookMapper;
import com.bookflow.book_flow.application.services.AuthorService;
import com.bookflow.book_flow.application.services.BookRelationService;
import com.bookflow.book_flow.domain.entities.Author;
import com.bookflow.book_flow.domain.entities.Book;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

  private final BookRelationService bookRelationService;
  private final AuthorService authorService;
  private final AuthorMapper authorMapper;
  private final BookMapper bookMapper;

  @GetMapping("/{authorId}/books")
  public ResponseEntity<List<BookResponse>> getAuthorBooks(@PathVariable Long authorId) {
    Author author = authorService.findById(authorId);
    List<Book> books = bookRelationService.findBooksByAuthor(author);
    List<BookResponse> bookResponses = bookMapper.toResponseList(books);
    return ResponseEntity.ok(bookResponses);
  }
}
