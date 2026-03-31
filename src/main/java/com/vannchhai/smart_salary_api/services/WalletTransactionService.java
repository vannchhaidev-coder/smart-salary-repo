package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.dto.request.TransactionRequest;
import com.vannchhai.smart_salary_api.dto.responses.PaginationDto;
import com.vannchhai.smart_salary_api.dto.responses.PaginationResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.TransactionDetailResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.WalletEmployeeTransactionResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.WalletTransactionResponse;
import java.util.UUID;

public interface WalletTransactionService {
  WalletTransactionResponse createTransaction(TransactionRequest request);

  PaginationResponse<WalletTransactionResponse> getTransactions(
      UUID walletId, PaginationDto pagination);

  PaginationResponse<TransactionDetailResponse> getTransactionsByEmployeeId(
      UUID employeeId, PaginationDto pagination);

  PaginationResponse<WalletEmployeeTransactionResponse> getEmployeeTransactions(
      UUID employeeId, PaginationDto paginationDto);
}
