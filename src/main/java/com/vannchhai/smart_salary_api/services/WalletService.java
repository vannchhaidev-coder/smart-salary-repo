package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.dto.request.WalletTransactionRequest;
import com.vannchhai.smart_salary_api.dto.responses.WalletBalanceResponse;
import com.vannchhai.smart_salary_api.dto.responses.WalletResponse;
import com.vannchhai.smart_salary_api.enums.ReferenceType;
import com.vannchhai.smart_salary_api.enums.TransactionType;
import com.vannchhai.smart_salary_api.models.WalletModel;
import com.vannchhai.smart_salary_api.models.WalletTransactionModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface WalletService {
  void credit(WalletModel wallet, BigDecimal amount);

  void debit(WalletModel wallet, BigDecimal amount);

  void applyTransaction(
      WalletModel wallet,
      TransactionType type,
      BigDecimal amount,
      ReferenceType referenceType,
      UUID referenceId,
      String description);

  WalletTransactionModel createTransaction(UUID walletId, WalletTransactionRequest request);

  List<WalletResponse> getAllWallets();

  WalletResponse getWalletByEmployeeId(String employeeCode);

  WalletBalanceResponse getWalletBalance(String employeeCode);
}
