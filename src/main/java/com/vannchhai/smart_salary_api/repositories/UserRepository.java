package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.UserModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
  Optional<UserModel> findByEmail(String email);

  boolean existsByUsername(String username);
}
