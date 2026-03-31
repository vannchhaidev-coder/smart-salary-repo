package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.enums.ReferenceType;
import com.vannchhai.smart_salary_api.enums.TransactionType;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.WalletModel;
import com.vannchhai.smart_salary_api.models.WalletTransactionModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.WalletRepository;
import com.vannchhai.smart_salary_api.repositories.WalletTransactionRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(10)
@Transactional
public class WalletDataLoading implements CommandLineRunner {

  private final WalletRepository walletRepository;
  private final WalletTransactionRepository transactionRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public void run(String... args) {

    List<EmployeeModel> employees = employeeRepository.findAll();

    for (EmployeeModel emp : employees) {

      WalletModel wallet =
          walletRepository
              .findByEmployee_Uuid(emp.getUuid())
              .orElseGet(
                  () ->
                      walletRepository.save(
                          WalletModel.builder().employee(emp).balance(BigDecimal.ZERO).build()));

      if (transactionRepository.findByWalletIdOrderByCreatedAtDesc(wallet.getId()).isEmpty()) {

        WalletTransactionModel payrollTransaction =
            WalletTransactionModel.builder()
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .amount(new BigDecimal("1500"))
                .transactionDate(LocalDateTime.of(2026, 3, 1, 0, 0))
                .referenceType(ReferenceType.PAYROLL)
                .referenceId(UUID.randomUUID())
                .description("Salary for March 2026")
                .build();

        WalletTransactionModel overtimeTransaction =
            WalletTransactionModel.builder()
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .amount(new BigDecimal("50"))
                .transactionDate(LocalDateTime.of(2026, 3, 1, 0, 0))
                .referenceType(ReferenceType.OVERTIME)
                .referenceId(UUID.randomUUID())
                .description("Overtime payment")
                .build();

        WalletTransactionModel adjustmentTransaction =
            WalletTransactionModel.builder()
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .amount(new BigDecimal("100"))
                .transactionDate(LocalDateTime.of(2026, 3, 1, 0, 0))
                .referenceType(ReferenceType.ADJUSTMENT)
                .referenceId(UUID.randomUUID())
                .description("Advance repayment")
                .build();

        WalletTransactionModel paymentTransaction =
            WalletTransactionModel.builder()
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .amount(new BigDecimal("100"))
                .transactionDate(LocalDateTime.of(2026, 3, 1, 0, 0))
                .referenceType(ReferenceType.ADJUSTMENT)
                .referenceId(UUID.randomUUID())
                .description("Advance repayment")
                .build();

        List<WalletTransactionModel> transactions =
            List.of(
                payrollTransaction, overtimeTransaction, adjustmentTransaction, paymentTransaction);

        transactionRepository.saveAll(transactions);

        BigDecimal balance = BigDecimal.ZERO;

        for (WalletTransactionModel tx : transactions) {
          if (tx.getTransactionType() == TransactionType.CREDIT) {
            balance = balance.add(tx.getAmount());
          } else {
            balance = balance.subtract(tx.getAmount());
          }
        }

        wallet.setBalance(balance);
        walletRepository.save(wallet);
        walletRepository.save(wallet);
      }
    }
  }
}
