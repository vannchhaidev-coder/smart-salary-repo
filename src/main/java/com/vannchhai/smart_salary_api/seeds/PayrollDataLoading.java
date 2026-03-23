package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.*;
import com.vannchhai.smart_salary_api.repositories.*;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(6)
public class PayrollDataLoading implements CommandLineRunner {

  private final PayrollRepository payrollRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public void run(String... args) {

    EmployeeModel emp1 = employeeRepository.findByEmployeeCode("EMP001").orElseThrow();
    EmployeeModel emp2 = employeeRepository.findByEmployeeCode("EMP002").orElseThrow();

    createPayrollIfNotExists(
        emp1, 3, 2026, new BigDecimal("1500"), new BigDecimal("200"), new BigDecimal("50"));

    createPayrollIfNotExists(
        emp2, 3, 2026, new BigDecimal("1200"), new BigDecimal("150"), new BigDecimal("40"));
  }

  private void createPayrollIfNotExists(
      EmployeeModel employee,
      Integer month,
      Integer year,
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
            .payMonth(month)
            .payYear(year)
            .baseSalary(baseSalary)
            .totalAllowance(allowance)
            .totalDeduction(deduction)
            .netSalary(netSalary)
            .build());
  }
}
