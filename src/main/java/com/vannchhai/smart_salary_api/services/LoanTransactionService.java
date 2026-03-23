package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.models.WalletTransactionModel;
import java.util.List;
import java.util.UUID;

public interface LoanTransactionService {
  List<WalletTransactionModel> getLoanTransactions(UUID loanId);
}
