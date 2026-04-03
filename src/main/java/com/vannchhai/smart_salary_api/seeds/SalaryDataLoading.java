package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.SalaryModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.SalaryRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(7)
// @Profile("dev")
public class SalaryDataLoading implements CommandLineRunner {

  private final EmployeeRepository employeeRepository;
  private final SalaryRepository salaryRepository;

  @Override
  public void run(String... args) {

    List<EmployeeModel> employees = employeeRepository.findAll();

    if (employees.isEmpty()) {
      System.out.println("No employees found. Skipping salary seed.");
      return;
    }

    for (EmployeeModel employee : employees) {

      BigDecimal baseSalary;
      BigDecimal allowance;

      String position = employee.getPosition().getTitle();

      switch (position) {
        case "Manager":
          baseSalary = new BigDecimal("2000");
          allowance = new BigDecimal("300");
          break;

        case "Engineer":
          baseSalary = new BigDecimal("1500");
          allowance = new BigDecimal("200");
          break;

        case "Accountant":
          baseSalary = new BigDecimal("1200");
          allowance = new BigDecimal("150");
          break;

        default:
          baseSalary = new BigDecimal("1000");
          allowance = new BigDecimal("100");
      }

      createSalaryIfNotExists(employee, baseSalary, allowance);
    }
  }

  private void createSalaryIfNotExists(
      EmployeeModel employee, BigDecimal baseSalary, BigDecimal allowance) {

    LocalDate effectiveDate = LocalDate.of(2026, 1, 1);

    if (salaryRepository.existsByEmployeeAndEffectiveDate(employee, effectiveDate)) {
      return;
    }

    salaryRepository.save(
        SalaryModel.builder()
            .employee(employee)
            .baseSalary(baseSalary)
            .allowance(allowance)
            .effectiveDate(effectiveDate)
            .build());
  }
}
