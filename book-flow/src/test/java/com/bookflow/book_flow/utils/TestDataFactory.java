package com.bookflow.book_flow.utils;

import com.bookflow.book_flow.domain.entities.Author;
import com.bookflow.book_flow.domain.entities.AuthorRole;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.entities.BookGenre;
import com.bookflow.book_flow.domain.entities.Genre;
import com.bookflow.book_flow.domain.entities.User;
import com.bookflow.book_flow.domain.enums.ContributionType;
import com.bookflow.book_flow.domain.enums.GenreIntensity;
import com.bookflow.book_flow.domain.enums.UserType;
import java.time.LocalDate;

public class TestDataFactory {

  public static Author createTestAuthor() {
    Author author = new Author();
    author.setFirstName("Marie");
    author.setLastName("Dubois");
    author.setNationality("FRANCE");
    author.setBirthDate(LocalDate.of(1995, 1, 1));

    return author;
  }

  public static Author createTestAuthor(String firstName, String lastName, String nationality,
      LocalDate birthDate) {
    Author author = new Author();
    author.setFirstName(firstName);
    author.setLastName(lastName);
    author.setNationality(nationality);
    author.setBirthDate(birthDate);
    return author;
  }

  public static Book createTestBook() {
    Book book = new Book();
    book.setIsbn("1111111111111");
    book.setTitle("My Test");
    book.setSubtitle("test is important");
    book.setDescription("Testing is not doubt, its important");
    return book;
  }

  public static Book createTestBook(String isbn, String title, String subtitle,
      String description) {
    Book book = new Book();
    book.setIsbn(isbn);
    book.setTitle(title);
    book.setSubtitle(subtitle);
    book.setDescription(description);
    return book;
  }

  public static Book createBookWithTitle(String title) {
    Book book = createTestBook("9785412000046", title, "", "");
    return book;
  }

  public static Book createBookWithIsbn(String isbn) {
    Book book = createTestBook(isbn, "Nous les dieux", "", "");
    return book;
  }

  public static User createTestUser() {
    User user = new User();
    user.setFirstName("Marie");
    user.setLastName("Dubois");
    user.setAddress("12 rue Test");
    user.setEmail("marie@dubois.com");
    user.setUserType(UserType.STUDENT);
    user.setMemberNumber(14587L);
    return user;
  }

  public static User createTestUser(String firstName, String lastName, String adress, String email,
      UserType userType, Long numberMember) {
    User user = new User();
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setAddress(adress);
    user.setEmail(email);
    user.setUserType(userType);
    user.setMemberNumber(numberMember);
    return user;
  }

  public static User createTestUserWithBirthDay(LocalDate birthDate) {
    User user = createTestUser();
    user.setBirthDate(birthDate);

    return user;
  }

  public static AuthorRole createTestAuthorRole(Book book, Author author,
      ContributionType contributionType) {
    AuthorRole authorRole = new AuthorRole();
    authorRole.setBook(book);
    authorRole.setAuthor(author);
    authorRole.setContributionType(contributionType);
    return authorRole;
  }

  public static Genre createTestGenre(String name, String description) {
    Genre genre = new Genre();
    genre.setName(name);
    genre.setDescription(description);
    return genre;
  }

  public static BookGenre createTestBookGenre(Book book, Genre genre, GenreIntensity intensity) {
    BookGenre bookGenre = new BookGenre();
    bookGenre.setBook(book);
    bookGenre.setGenre(genre);
    bookGenre.setIntensity(intensity);
    return bookGenre;
  }

}
