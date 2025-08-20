package com.bookflow.book_flow.infrastructure.controllers;

import com.bookflow.book_flow.application.dto.request.BookRequest;
import com.bookflow.book_flow.application.dto.response.AuthorResponse;
import com.bookflow.book_flow.application.dto.response.BookResponse;
import com.bookflow.book_flow.application.dto.response.GenreResponse;
import com.bookflow.book_flow.application.mappers.AuthorMapper;
import com.bookflow.book_flow.application.mappers.BookMapper;
import com.bookflow.book_flow.application.mappers.GenreMapper;
import com.bookflow.book_flow.application.services.AuthorService;
import com.bookflow.book_flow.application.services.BookRelationService;
import com.bookflow.book_flow.application.services.BookService;
import com.bookflow.book_flow.domain.entities.Author;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.entities.Genre;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;
  private final BookRelationService bookRelationService;
  private final BookMapper bookMapper;
  private final AuthorMapper authorMapper;
  private final GenreMapper genreMapper;
  private final AuthorService authorService;

  @GetMapping
  public ResponseEntity<List<BookResponse>> getAllBooks() {
    List<Book> books = bookService.findAll();
    List<BookResponse> enrichedBooks = books.stream()
        .map(this::enrichBookWithRelations)
        .toList();
    return ResponseEntity.ok(enrichedBooks);
  }

  @GetMapping("/{isbn}")
  public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
    Optional<Book> book = bookService.findByIsbn(isbn);
    if (book.isPresent()) {
      BookResponse bookResponse = bookMapper.toResponse(book.get());
      return ResponseEntity.ok(bookResponse);
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public ResponseEntity<List<BookResponse>> searchBooksByTitle(@RequestParam String title) {
    List<Book> books = bookService.searchAllByTitle(title);
    List<BookResponse> bookResponse = bookMapper.toResponseList(books);
    return ResponseEntity.ok(bookResponse);
  }

  @GetMapping("/by-author/{authorId}")
  public ResponseEntity<List<BookResponse>> getBooksByAuthor(@PathVariable Long authorId) {
    Author author = authorService.findById(authorId);
    List<Book> books = bookRelationService.findBooksByAuthor(author);
    List<BookResponse> bookResponses = bookMapper.toResponseList(books);
    return ResponseEntity.ok(bookResponses);
  }

  @GetMapping("/{bookId}/authors")
  public ResponseEntity<List<AuthorResponse>> getBookAuthors(@PathVariable Long bookId) {
    Book book = bookService.findById(bookId);
    List<Author> authors = bookRelationService.findAuthorsByBook(book);
    List<AuthorResponse> authorResponses = authorMapper.toResponseList(authors);
    return ResponseEntity.ok(authorResponses);
  }

  @GetMapping("/{bookId}/genres")
  public ResponseEntity<List<GenreResponse>> getBookGenres(@PathVariable Long bookId) {
    Book book = bookService.findById(bookId);
    List<Genre> genres = bookRelationService.findGenresByBook(book);
    List<GenreResponse> genreResponses = genreMapper.toResponseList(genres);
    return ResponseEntity.ok(genreResponses);
  }

  @PostMapping
  public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
    Book book = bookMapper.toEntity(request);
    Book savedBook = bookService.save(book);
    BookResponse bookResponse = bookMapper.toResponse(savedBook);
    return ResponseEntity.status(HttpStatus.CREATED).body(bookResponse);
  }

  private BookResponse enrichBookWithRelations(Book book) {
    BookResponse response = bookMapper.toResponse(book);
    
    List<Author> authors = bookRelationService.findAuthorsByBook(book);
    List<Genre> genres = bookRelationService.findGenresByBook(book);

    response.setAuthors(authorMapper.toResponseList(authors));
    response.setGenres(genreMapper.toResponseList(genres));

    return response;
  }
}
