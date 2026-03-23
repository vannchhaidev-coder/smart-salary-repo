package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.UserRoleModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleModel, Long> {
  boolean existsByRoleName(String roleName);

  Optional<UserRoleModel> findByRoleName(String roleName);
}
