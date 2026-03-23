package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.dto.request.TransactionRequest;
import com.vannchhai.smart_salary_api.dto.responses.WalletTransactionResponse;

import java.util.List;
import java.util.UUID;

public interface WalletTransactionService {
    WalletTransactionResponse createTransaction(TransactionRequest request);

    List<WalletTransactionResponse> getTransactions(UUID employeeId);
}
