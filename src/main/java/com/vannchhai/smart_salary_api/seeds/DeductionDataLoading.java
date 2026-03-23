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

    for (EmployeeModel emp : employees) {

      if (!deductionRepository
          .findByEmployeeIdAndDeductionDate(emp.getId(), LocalDate.of(2026, 3, 5))
          .isEmpty()) {
        continue;
      }

      DeductionModel taxDeduction =
          DeductionModel.builder()
              .employee(emp)
              .deductionType(DeductionType.TAX)
              .amount(new BigDecimal("150"))
              .deductionDate(LocalDate.of(2026, 3, 5))
              .build();

      DeductionModel loanDeduction =
          DeductionModel.builder()
              .employee(emp)
              .deductionType(DeductionType.LOAN)
              .amount(new BigDecimal("200"))
              .deductionDate(LocalDate.of(2026, 3, 5))
              .build();

      DeductionModel penaltyDeduction =
          DeductionModel.builder()
              .employee(emp)
              .deductionType(DeductionType.PENALTY)
              .amount(new BigDecimal("50"))
              .deductionDate(LocalDate.of(2026, 3, 5))
              .build();

      deductionRepository.saveAll(List.of(taxDeduction, loanDeduction, penaltyDeduction));
    }
  }
}
