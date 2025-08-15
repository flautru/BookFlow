package com.bookflow.book_flow.application.dto.response;

import java.time.LocalDate;

public record AuthorResponse(Long id, String firstName, String lastName, String nationality,
                             LocalDate birthDate) {

}
