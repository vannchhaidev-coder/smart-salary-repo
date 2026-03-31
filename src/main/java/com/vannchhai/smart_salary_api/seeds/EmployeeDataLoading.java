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
import java.util.List;
import java.util.Random;
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

    List<UserModel> users = userRepository.findAll();
    List<DepartmentModel> departments = departmentRepository.findAll();
    List<PositionModel> positions = positionRepository.findAll();
    Random random = new Random();

    for (UserModel user : users) {
      if (employeeRepository.existsByUser(user)) {
        continue;
      }

      DepartmentModel department = departments.get(random.nextInt(departments.size()));
      PositionModel position = positions.get(random.nextInt(positions.size()));

      // Ensure unique employee code
      String employeeCode = generateUniqueEmployeeCode();

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

  private String generateUniqueEmployeeCode() {
    String prefix = "EMP";
    String code;
    Random random = new Random();

    do {
      int randomNum = random.nextInt(900) + 100;
      code = prefix + randomNum;
    } while (employeeRepository.existsByEmployeeCode(code));

    return code;
  }
}
