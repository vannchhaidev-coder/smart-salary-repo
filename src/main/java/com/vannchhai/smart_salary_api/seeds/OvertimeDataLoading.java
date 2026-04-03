package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.OvertimeModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.OvertimeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(9)
// @Profile("dev")
public class OvertimeDataLoading implements CommandLineRunner {

  private final OvertimeRepository overtimeRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public void run(String... args) {

    List<EmployeeModel> employees = employeeRepository.findAll();

    if (employees.isEmpty()) {
      System.out.println("No employees found, skipping overtime seeding.");
      return;
    }

    // Example: generate overtime for March 2026
    LocalDate startDate = LocalDate.of(2026, 3, 1);
    LocalDate endDate = LocalDate.of(2026, 3, 31);

    for (EmployeeModel employee : employees) {
      LocalDate currentDate = startDate;

      while (!currentDate.isAfter(endDate)) {
        // Skip weekends
        if (currentDate.getDayOfWeek().getValue() <= 5) {
          createOvertimeIfNotExists(
              employee, currentDate, new BigDecimal("2"), new BigDecimal("10"));
        }
        currentDate = currentDate.plusDays(1);
      }

      System.out.println(
          "Overtime generated for " + employee.getEmployeeCode() + " for March 2026");
    }
  }

  private void createOvertimeIfNotExists(
      EmployeeModel employee, LocalDate date, BigDecimal hours, BigDecimal rate) {
    if (overtimeRepository.existsByEmployeeAndWorkDate(employee, date)) {
      return;
    }

    overtimeRepository.save(
        OvertimeModel.builder().employee(employee).workDate(date).hours(hours).rate(rate).build());
  }
}
