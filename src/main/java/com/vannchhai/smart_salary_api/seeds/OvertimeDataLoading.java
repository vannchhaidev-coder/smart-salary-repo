package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.OvertimeModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.OvertimeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(9)
public class OvertimeDataLoading implements CommandLineRunner {

  private final OvertimeRepository overtimeRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public void run(String... args) {

    EmployeeModel emp1 = employeeRepository.findByEmployeeCode("EMP001").orElseThrow();
    EmployeeModel emp2 = employeeRepository.findByEmployeeCode("EMP002").orElseThrow();

    createOvertime(emp1, LocalDate.of(2026, 3, 5), new BigDecimal("2"), new BigDecimal("10"));
    createOvertime(emp2, LocalDate.of(2026, 3, 6), new BigDecimal("3"), new BigDecimal("10"));
  }

  private void createOvertime(
      EmployeeModel employee, LocalDate date, BigDecimal hours, BigDecimal rate) {

    if (overtimeRepository.existsByEmployeeAndWorkDate(employee, date)) {
      return;
    }

    overtimeRepository.save(
        OvertimeModel.builder().employee(employee).workDate(date).hours(hours).rate(rate).build());
  }
}
