package com.bookflow.book_flow.domain.repositories;

import com.bookflow.book_flow.domain.entities.User;
import com.bookflow.book_flow.domain.enums.UserType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByMemberNumber(Long memberNumber);

  List<User> findAllByUserType(UserType userType);

  boolean existsByEmail(String email);


}
