package com.bookflow.book_flow.application.dto.response;

import com.bookflow.book_flow.domain.enums.ContributionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorRoleResponse {

  private Long authorId;
  private String firstName;
  private String lastName;
  private String nationality;
  private ContributionType role;
}

