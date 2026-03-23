package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.enums.ReferenceType;
import com.vannchhai.smart_salary_api.exceptions.ResourceNotFoundException;
import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.models.WalletTransactionModel;
import com.vannchhai.smart_salary_api.repositories.LoanRepository;
import com.vannchhai.smart_salary_api.repositories.WalletTransactionRepository;
import com.vannchhai.smart_salary_api.services.LoanTransactionService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanTransactionServiceImpl implements LoanTransactionService {

  private final WalletTransactionRepository walletTransactionRepository;
  private final LoanRepository loanRepository;

  @Override
  public List<WalletTransactionModel> getLoanTransactions(UUID loanId) {

    LoanModel loan =
        loanRepository
            .findByUuid(loanId)
            .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", loanId));

    return walletTransactionRepository.findByReferenceTypeAndReferenceId(
        ReferenceType.LOAN, loan.getUuid());
  }
}
