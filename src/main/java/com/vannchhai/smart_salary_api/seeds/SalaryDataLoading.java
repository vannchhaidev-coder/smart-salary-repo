package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.SalaryModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.SalaryRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(7)
public class SalaryDataLoading implements CommandLineRunner {

  private final SalaryRepository salaryRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public void run(String... args) {

    EmployeeModel emp1 = employeeRepository.findByEmployeeCode("EMP001").orElseThrow();
    EmployeeModel emp2 = employeeRepository.findByEmployeeCode("EMP002").orElseThrow();

    createSalaryIfNotExists(emp1, new BigDecimal("1500"), new BigDecimal("200"));
    createSalaryIfNotExists(emp2, new BigDecimal("1200"), new BigDecimal("150"));
  }

  private void createSalaryIfNotExists(
      EmployeeModel employee, BigDecimal baseSalary, BigDecimal allowance) {

    LocalDate effectiveDate = LocalDate.of(2026, 1, 1);

    if (salaryRepository.existsByEmployeeIdAndEffectiveDate(employee.getId(), effectiveDate)) {
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
