package com.bookflow.book_flow.application.validators;

import com.bookflow.book_flow.application.dto.request.BookRequest;
import com.bookflow.book_flow.application.exceptions.InvalidReferenceException;
import com.bookflow.book_flow.domain.repositories.BookRepository;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookRequestValidator {

  private static final Logger logger = LoggerFactory.getLogger(BookRequestValidator.class);

  private static final Pattern ISBN_DIGIT_PATTERN = Pattern.compile("^\\d{13}$");
  private static final Pattern XSS_PATTERN = Pattern.compile(".*[<>\"'&;].*");
  private static final Pattern SUSPICIOUS_PATTERN = Pattern.compile(
      ".*(?i)(script|javascript|vbscript|onload|onerror).*");

  private final BookRepository bookRepository;

  public void validate(BookRequest request) {
    logger.debug("Starting validation for BookRequest: {}", request);

    validateSecurity(request);
    validateBusinessRules(request);

    logger.debug("Validation completed successfully for ISBN: {}", request.getIsbn());
  }

  public void validateSecurity(BookRequest request) {
    logger.debug("Validating security constraints");

    validateNoXssCharacters("title", request.getTitle());
    validateNoXssCharacters("subtitle", request.getSubtitle());
    validateNoXssCharacters("description", request.getDescription());

    validateNoSuspiciousContent("title", request.getTitle());
    validateNoSuspiciousContent("subtitle", request.getSubtitle());
    validateNoSuspiciousContent("description", request.getDescription());
  }

  public void validateBusinessRules(BookRequest request) {
    logger.debug("Validating business rules");

    validateIsbnFormat(request.getIsbn());
    validateTitleContent(request.getTitle());
    validateCoherence(request);
  }

  private void validateIsbnFormat(String isbn) {
    if (isbn == null) {
      return; // Déjà géré par @NotBlank
    }

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

  private void validateCoherence(BookRequest request) {
    if (request.getSubtitle() != null && !request.getSubtitle().trim().isEmpty()) {
      if (request.getTitle().equals(request.getSubtitle())) {
        throw new InvalidReferenceException("Subtitle must be different from title");
      }
    }

    if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
      if (request.getTitle().equals(request.getDescription().trim())) {
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
          String.format("Field '%s' contains invalid characters: < > \" ' & ;", fieldName)
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
