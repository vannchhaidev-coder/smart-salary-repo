package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.enums.DeductionType;
import com.vannchhai.smart_salary_api.models.DeductionModel;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.repositories.DeductionRepository;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(11)
public class DeductionDataLoading implements CommandLineRunner {

  private final DeductionRepository deductionRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public void run(String... args) {

    List<EmployeeModel> employees = employeeRepository.findAll();

    LocalDate deductionDate = LocalDate.of(2026, 3, 5);

    for (EmployeeModel emp : employees) {

      if (!deductionRepository
          .findByEmployeeIdAndDeductionDate(emp.getId(), deductionDate)
          .isEmpty()) {
        continue;
      }

      List<DeductionModel> deductions =
          List.of(
              createDeduction(emp, DeductionType.TAX, new BigDecimal("150"), deductionDate),
              createDeduction(emp, DeductionType.LOAN, new BigDecimal("200"), deductionDate),
              createDeduction(emp, DeductionType.PENALTY, new BigDecimal("50"), deductionDate));

      deductionRepository.saveAll(deductions);
      System.out.println(
          "Deductions created for " + emp.getEmployeeCode() + " on " + deductionDate);
    }
  }

  private DeductionModel createDeduction(
      EmployeeModel emp, DeductionType type, BigDecimal amount, LocalDate date) {
    return DeductionModel.builder()
        .employee(emp)
        .deductionType(type)
        .amount(amount)
        .deductionDate(date)
        .build();
  }
}
