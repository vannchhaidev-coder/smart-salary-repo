package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.models.LoanPaymentModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.LoanPaymentRepository;
import com.vannchhai.smart_salary_api.repositories.LoanRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(12)
// @Profile("dev")
public class LoanDataLoading implements CommandLineRunner {

  private final EmployeeRepository employeeRepository;
  private final LoanRepository loanRepository;
  private final LoanPaymentRepository paymentRepository;

  @Override
  public void run(String... args) {

    Set<String> excludedEmails = Set.of("vannchhai-dev@gmail.com", "admin@example.com");

    List<EmployeeModel> employees =
        employeeRepository.findAll().stream()
            .filter(emp -> !excludedEmails.contains(emp.getUser().getEmail()))
            .toList();

    Random random = new Random();

    // Define months for 2026
    LocalDate[] loanStartDates =
        new LocalDate[] {
          LocalDate.of(2026, 1, 5),
          LocalDate.of(2026, 2, 10),
          LocalDate.of(2026, 3, 15),
          LocalDate.of(2026, 4, 20),
          LocalDate.of(2026, 5, 10),
          LocalDate.of(2026, 6, 15),
          LocalDate.of(2026, 7, 5),
          LocalDate.of(2026, 8, 12),
          LocalDate.of(2026, 9, 18),
          LocalDate.of(2026, 10, 5),
          LocalDate.of(2026, 11, 10),
          LocalDate.of(2026, 12, 15)
        };

    LoanStatus[] statuses = LoanStatus.values();
    String[] riskLevels = {"low", "medium", "high"};

    for (EmployeeModel emp : employees) {

      // Give each employee 2–4 loans
      int loanCount = random.nextInt(3) + 2;

      for (int i = 0; i < loanCount; i++) {

        LocalDate startDate = loanStartDates[random.nextInt(loanStartDates.length)];
        LocalDate endDate = startDate.plusMonths(3);

        LoanStatus status = statuses[random.nextInt(statuses.length)];
        String riskLevel = riskLevels[random.nextInt(riskLevels.length)];

        BigDecimal amount = BigDecimal.valueOf(1000 + random.nextInt(5000)); // 1000–6000
        BigDecimal interestRate = BigDecimal.valueOf(5 + random.nextInt(6)); // 5–10%
        BigDecimal paidAmount =
            status == LoanStatus.COMPLETED
                ? amount
                : BigDecimal.valueOf(random.nextInt(amount.intValue()));

        LoanModel loan =
            LoanModel.builder()
                .employee(emp)
                .amount(amount)
                .paidAmount(paidAmount)
                .remainingBalance(amount.subtract(paidAmount))
                .reason("Emergency")
                .interestRate(interestRate)
                .riskScore(50 + random.nextInt(51)) // 50–100
                .riskLevel(riskLevel)
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .approvedBy("HR manager")
                .build();

        loanRepository.save(loan);

        // Add loan payments for approved/repaying/completed loans
        if (status == LoanStatus.APPROVED
            || status == LoanStatus.REPAYING
            || status == LoanStatus.COMPLETED) {

          int paymentCount = 2 + random.nextInt(3); // 2–4 payments
          for (int j = 0; j < paymentCount; j++) {
            LocalDate paymentDate = startDate.plusDays(15L * (j + 1));
            BigDecimal paymentAmount =
                amount.divide(BigDecimal.valueOf(paymentCount), 2, RoundingMode.HALF_UP);

            paymentRepository.save(
                LoanPaymentModel.builder()
                    .loan(loan)
                    .amount(paymentAmount)
                    .paymentDate(paymentDate)
                    .build());
          }
        }
      }

      System.out.println("Loans created for employee: " + emp.getEmployeeCode());
    }
  }
}
