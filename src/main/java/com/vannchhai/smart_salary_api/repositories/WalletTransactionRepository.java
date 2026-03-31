package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.enums.ReferenceType;
import com.vannchhai.smart_salary_api.models.WalletModel;
import com.vannchhai.smart_salary_api.models.WalletTransactionModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransactionModel, Long> {

  List<WalletTransactionModel> findByWalletIdOrderByCreatedAtDesc(Long walletId);

  List<WalletTransactionModel> findByWallet_Employee_EmployeeCode(String employeeCode);

  Page<WalletTransactionModel> findByWallet(WalletModel wallet, Pageable pageable);

  @Query(
      """
        SELECT COALESCE(SUM(t.amount), 0)
        FROM WalletTransactionModel t
        WHERE t.referenceType = 'LOAN_PAYMENT'
          AND t.referenceId = :loanUuid
    """)
  BigDecimal getTotalLoanPayment(@Param("loanUuid") UUID loanUuid);

  List<WalletTransactionModel> findByReferenceTypeAndReferenceId(
      ReferenceType referenceType, UUID referenceId);

  List<WalletTransactionModel> findByWallet(WalletModel wallet);

  Page<WalletTransactionModel> findByWallet_Employee_Uuid(UUID employeeUuid, Pageable pageable);
}
