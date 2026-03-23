package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.DepartmentModel;
import com.vannchhai.smart_salary_api.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(3)
public class DepartmentDataLoading implements CommandLineRunner {

  private final DepartmentRepository departmentRepository;

  @Override
  public void run(String... args) {

    createDepartmentIfNotExists("IT", "Information Technology Department");
    createDepartmentIfNotExists("HR", "Human Resource Department");
    createDepartmentIfNotExists("Finance", "Finance Department");
    createDepartmentIfNotExists("Marketing", "Marketing Department");
  }

  private void createDepartmentIfNotExists(String name, String description) {

    if (departmentRepository.existsByName(name)) {
      return;
    }

    departmentRepository.save(
        DepartmentModel.builder().name(name).description(description).build());
  }
}
