package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.models.LoanPaymentModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.LoanPaymentRepository;
import com.vannchhai.smart_salary_api.repositories.LoanRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(12)
public class LoanDataLoading implements CommandLineRunner {

  private final EmployeeRepository employeeRepository;
  private final LoanRepository loanRepository;
  private final LoanPaymentRepository paymentRepository;

  @Override
  public void run(String... args) {

    employeeRepository
        .findAll()
        .forEach(
            emp -> {
              if (!loanRepository
                  .findByEmployeeIdAndStatus(emp.getId(), LoanStatus.APPROVED)
                  .isEmpty()) return;

              LoanModel loan =
                  LoanModel.builder()
                      .employee(emp)
                      .amount(new BigDecimal("1000"))
                      .reason("Emergency")
                      .interestRate(new BigDecimal("5"))
                      .paidAmount(new BigDecimal("1000"))
                      .remainingBalance(new BigDecimal("1500"))
                      .riskScore(80)
                      .riskLevel("low")
                      .startDate(LocalDate.of(2026, 3, 1))
                      .endDate(LocalDate.of(2026, 6, 1))
                      .status(LoanStatus.APPROVED)
                      .build();
              loanRepository.save(loan);

              LoanPaymentModel payment1 =
                  LoanPaymentModel.builder()
                      .loan(loan)
                      .amount(new BigDecimal("250"))
                      .paymentDate(LocalDate.of(2026, 3, 15))
                      .build();

              LoanPaymentModel payment2 =
                  LoanPaymentModel.builder()
                      .loan(loan)
                      .amount(new BigDecimal("250"))
                      .paymentDate(LocalDate.of(2026, 4, 15))
                      .build();

              paymentRepository.saveAll(List.of(payment1, payment2));
            });
  }
}
