package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.enums.UserStatus;
import com.vannchhai.smart_salary_api.models.UserModel;
import com.vannchhai.smart_salary_api.models.UserRoleModel;
import com.vannchhai.smart_salary_api.repositories.UserRepository;
import com.vannchhai.smart_salary_api.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2)
// @Profile("dev")
public class UserDataLoading implements CommandLineRunner {

  private final UserRepository userRepository;
  private final UserRoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {

    UserRoleModel adminRole = roleRepository.findByRoleName("ROLE_ADMIN").orElseThrow();
    UserRoleModel employeeRole = roleRepository.findByRoleName("ROLE_EMPLOYEE").orElseThrow();

    createUserIfNotExists("HR manager", "admin@example.com", "password123", adminRole);
    createUserIfNotExists("Rothna Chan", "rothna@example.com", "employee@123", employeeRole);
    createUserIfNotExists("Bopha Keo", "bopha@company.com", "employee@123", employeeRole);
    createUserIfNotExists("Vann Nak", "vannak@example.com", "employee@123", employeeRole);
    createUserIfNotExists("Nary Chan", "nary@company.com", "employee@123", employeeRole);
    createUserIfNotExists("Sok Chheav", "sokchheav@example.com", "employee@123", employeeRole);
    createUserIfNotExists("Sokly Seng", "sokly@company.com", "employee@123", employeeRole);
    createUserIfNotExists("Sok Chheav", "sokchheav12@example.com", "employee@123", employeeRole);
    createUserIfNotExists("Sokly Seng", "sokly03@company.com", "employee@123", employeeRole);
    createUserIfNotExists("Bopha Keo", "bopha01@company.com", "employee@123", employeeRole);
    createUserIfNotExists("Vann Nak", "vannak04@example.com", "employee@123", employeeRole);
    createUserIfNotExists("Vann Chhai", "vannchhai-dev@gmail.com", "employee@123", employeeRole);
  }

  private void createUserIfNotExists(
      String name, String email, String password, UserRoleModel role) {

    userRepository
        .findByEmail(email)
        .orElseGet(
            () ->
                userRepository.save(
                    UserModel.builder()
                        .name(name)
                        .email(email)
                        .passwordHash(passwordEncoder.encode(password))
                        .role(role)
                        .status(UserStatus.ACTIVE)
                        .build()));
  }
}
