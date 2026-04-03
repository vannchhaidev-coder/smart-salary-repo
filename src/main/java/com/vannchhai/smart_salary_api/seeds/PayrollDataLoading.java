package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.*;
import com.vannchhai.smart_salary_api.repositories.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(6)
// @Profile("dev")
public class PayrollDataLoading implements CommandLineRunner {

  private final PayrollRepository payrollRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public void run(String... args) {

    List<EmployeeModel> employees = employeeRepository.findAll();

    if (employees.isEmpty()) {
      System.out.println("No employees found, skipping payroll seeding.");
      return;
    }

    LocalDateTime now = LocalDateTime.now();
    int month = now.getMonthValue();
    int year = now.getYear();

    for (EmployeeModel employee : employees) {
      // Example: compute base salary and allowance based on position or default
      BigDecimal baseSalary =
          employee.getPosition() != null
              ? employee.getPosition().getBaseSalary()
              : BigDecimal.valueOf(1000);
      BigDecimal allowance = BigDecimal.valueOf(100); // default allowance
      BigDecimal deduction = BigDecimal.valueOf(50); // default deduction

      createPayrollIfNotExists(
          employee, "processed", month, year, now, baseSalary, allowance, deduction);
    }
  }

  private void createPayrollIfNotExists(
      EmployeeModel employee,
      String status,
      Integer month,
      Integer year,
      LocalDateTime processedDate,
      BigDecimal baseSalary,
      BigDecimal allowance,
      BigDecimal deduction) {

    if (payrollRepository.existsByEmployeeIdAndPayMonthAndPayYear(employee.getId(), month, year)) {
      return;
    }

    BigDecimal netSalary = baseSalary.add(allowance).subtract(deduction);

    payrollRepository.save(
        PayrollModel.builder()
            .employee(employee)
            .status(status)
            .payMonth(month)
            .payYear(year)
            .processedDate(processedDate)
            .baseSalary(baseSalary)
            .totalAllowance(allowance)
            .totalDeduction(deduction)
            .netSalary(netSalary)
            .build());
  }
}
