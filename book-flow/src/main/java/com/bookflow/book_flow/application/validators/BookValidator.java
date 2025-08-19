package com.bookflow.book_flow.application.validators;

import com.bookflow.book_flow.application.exceptions.InvalidReferenceException;
import com.bookflow.book_flow.domain.entities.Book;
import com.bookflow.book_flow.domain.repositories.BookRepository;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookValidator {

  private static final Logger logger = LoggerFactory.getLogger(BookValidator.class);

  private static final Pattern ISBN_DIGIT_PATTERN = Pattern.compile("^\\d{13}$");
  private static final Pattern XSS_PATTERN = Pattern.compile(".*[<>\";].*");

  // ⚠️ LIMITATION CONNUE : Certains livres techniques légitimes sont bloqués
  // Exemples : "JavaScript pour les nuls", "Learning script programming"
  //
  // DÉCISION : Prioriser la sécurité pour le MVP
  // TODO FUTUR : Affiner la détection (whitelist, contexte, patterns spécifiques)
  private static final Pattern SUSPICIOUS_PATTERN = Pattern.compile(
      ".*(?i)\\b(script|javascript|vbscript|onload|onerror)\\b.*");

  private final BookRepository bookRepository;

  public void validate(Book book) {
    logger.debug("Starting validation for BookRequest: {}", book);

    validateSecurity(book);
    validateBusinessRules(book);

    logger.debug("Validation completed successfully for ISBN: {}", book.getIsbn());
  }

  public void validateSecurity(Book book) {
    logger.debug("Validating security constraints");

    validateNoXssCharacters("title", book.getTitle());
    validateNoXssCharacters("subtitle", book.getSubtitle());
    validateNoXssCharacters("description", book.getDescription());

    validateNoSuspiciousContent("title", book.getTitle());
    validateNoSuspiciousContent("subtitle", book.getSubtitle());
    validateNoSuspiciousContent("description", book.getDescription());
  }

  public void validateBusinessRules(Book book) {
    logger.debug("Validating business rules");

    validateIsbnFormat(book.getIsbn());
    validateTitleContent(book.getTitle());
    validateCoherence(book);
  }

  private void validateIsbnFormat(String isbn) {

    if (!ISBN_DIGIT_PATTERN.matcher(isbn).matches()) {
      throw new InvalidReferenceException(
          "ISBN must contain exactly 13 digits without spaces or dashes");
    }

    if (!isValidIsbn13Checksum(isbn)) {
      logger.warn("Invalid ISBN-13 checksum for: {}", isbn);
      throw new InvalidReferenceException("ISBN checksum is invalid");
    }
  }

  private void validateTitleContent(String title) {
    if (title == null || title.trim().isEmpty()) {
      return; // Déjà géré par @NotBlank
    }

    String trimmedTitle = title.trim();

    // Titre ne peut pas être que des espaces ou caractères spéciaux
    if (trimmedTitle.matches("^[\\s\\p{Punct}]*$")) {
      throw new InvalidReferenceException("Title must contain meaningful content");
    }

    if (trimmedTitle.startsWith(".") || trimmedTitle.endsWith(".") ||
        trimmedTitle.startsWith("-") || trimmedTitle.endsWith("-")) {
      throw new InvalidReferenceException("Title format is invalid");
    }
  }

  private void validateCoherence(Book book) {
    if (book.getSubtitle() != null && !book.getSubtitle().trim().isEmpty()) {
      if (book.getTitle().equals(book.getSubtitle())) {
        throw new InvalidReferenceException("Subtitle must be different from title");
      }
    }

    if (book.getDescription() != null && !book.getDescription().trim().isEmpty()) {
      if (book.getTitle().equals(book.getDescription().trim())) {
        throw new InvalidReferenceException("Description must be different from title");
      }
    }
  }

  private void validateNoXssCharacters(String fieldName, String value) {
    if (value == null) {
      return;
    }

    if (XSS_PATTERN.matcher(value).matches()) {
      logger.warn("XSS characters detected in field '{}': {}", fieldName, value);
      throw new InvalidReferenceException(
          String.format("Field '%s' contains invalid characters: < > \" ;", fieldName)
      );
    }
  }

  private void validateNoSuspiciousContent(String fieldName, String value) {
    if (value == null) {
      return;
    }

    if (SUSPICIOUS_PATTERN.matcher(value).matches()) {
      logger.warn("Suspicious content detected in field '{}': {}", fieldName, value);
      throw new InvalidReferenceException(
          String.format("Field '%s' contains suspicious content", fieldName)
      );
    }
  }

  private boolean isValidIsbn13Checksum(String isbn) {
    try {
      int sum = 0;
      for (int i = 0; i < 12; i++) {
        int digit = Character.getNumericValue(isbn.charAt(i));
        sum += (i % 2 == 0) ? digit : digit * 3;
      }

      int checkDigit = (10 - (sum % 10)) % 10;
      int providedCheckDigit = Character.getNumericValue(isbn.charAt(12));

      return checkDigit == providedCheckDigit;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
