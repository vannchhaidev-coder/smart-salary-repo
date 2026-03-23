package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.enums.Badge;
import com.vannchhai.smart_salary_api.models.DepartmentModel;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.PositionModel;
import com.vannchhai.smart_salary_api.models.UserModel;
import com.vannchhai.smart_salary_api.repositories.DepartmentRepository;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.PositionRepository;
import com.vannchhai.smart_salary_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(5)
public class EmployeeDataLoading implements CommandLineRunner {

  private final EmployeeRepository employeeRepository;
  private final UserRepository userRepository;
  private final DepartmentRepository departmentRepository;
  private final PositionRepository positionRepository;

  @Override
  public void run(String... args) {

    UserModel admin = userRepository.findByEmail("admin@example.com").orElseThrow();
    UserModel user = userRepository.findByEmail("user@example.com").orElseThrow();
    UserModel employee = userRepository.findByEmail("vannchhai-dev@gmail.com").orElseThrow();

    DepartmentModel it = departmentRepository.findByName("IT").orElseThrow();
    DepartmentModel hr = departmentRepository.findByName("HR").orElseThrow();

    PositionModel engineer = positionRepository.findByTitle("Software Engineer").orElseThrow();
    PositionModel manager = positionRepository.findByTitle("HR Manager").orElseThrow();

    createEmployeeIfNotExists(admin, "EMP001", it, engineer);
    createEmployeeIfNotExists(user, "EMP002", hr, manager);
    createEmployeeIfNotExists(employee, "EMP003", it, engineer);
  }

  private void createEmployeeIfNotExists(
      UserModel user, String employeeCode, DepartmentModel department, PositionModel position) {

    if (employeeRepository.existsByEmployeeCode(employeeCode)) {
      return;
    }

    employeeRepository.save(
        EmployeeModel.builder()
            .user(user)
            .employeeCode(employeeCode)
            .department(department)
            .position(position)
            .badge(Badge.EXCELLENT)
            .build());
  }
}
