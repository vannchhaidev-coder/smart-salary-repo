package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.UserRoleModel;
import com.vannchhai.smart_salary_api.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
// @Profile("dev")
public class UserRoleDataLoading implements CommandLineRunner {

  private final UserRoleRepository roleRepository;

  @Override
  public void run(String... args) {

    createRoleIfNotExists("ROLE_ADMIN", "Administrator role");
    createRoleIfNotExists("ROLE_USER", "Normal user role");
    createRoleIfNotExists("ROLE_EMPLOYEE", "Normal employee role");
  }

  private void createRoleIfNotExists(String roleName, String description) {

    if (roleRepository.existsByRoleName(roleName)) {
      return;
    }

    roleRepository
        .findByRoleName(roleName)
        .orElseGet(
            () ->
                roleRepository.save(
                    UserRoleModel.builder().roleName(roleName).description(description).build()));
  }
}
