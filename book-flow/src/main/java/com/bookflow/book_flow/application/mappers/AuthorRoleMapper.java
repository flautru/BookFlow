package com.bookflow.book_flow.application.mappers;

import com.bookflow.book_flow.application.dto.response.AuthorRoleResponse;
import com.bookflow.book_flow.domain.entities.AuthorRole;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AuthorRoleMapper {

  public AuthorRoleResponse toResponse(AuthorRole authorRole) {
    if (authorRole == null) {
      return null;
    }

    return new AuthorRoleResponse(
        authorRole.getId(),
        authorRole.getAuthor().getFirstName(),
        authorRole.getAuthor().getLastName(),
        authorRole.getAuthor().getNationality(),
        authorRole.getContributionType()
    );
  }

  public List<AuthorRoleResponse> toResponseList(List<AuthorRole> authorRoles) {
    if (authorRoles == null) {
      return null;
    }
    return authorRoles.stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }
}