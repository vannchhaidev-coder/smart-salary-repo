package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.dto.request.TransactionRequest;
import com.vannchhai.smart_salary_api.dto.responses.WalletTransactionResponse;
import com.vannchhai.smart_salary_api.enums.TransactionType;
import com.vannchhai.smart_salary_api.exceptions.ResourceNotFoundException;
import com.vannchhai.smart_salary_api.mapper.WalletTransactionMapper;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.WalletModel;
import com.vannchhai.smart_salary_api.models.WalletTransactionModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.WalletRepository;
import com.vannchhai.smart_salary_api.repositories.WalletTransactionRepository;
import com.vannchhai.smart_salary_api.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

  private final WalletTransactionRepository walletTransactionRepository;
  private final WalletRepository walletRepository;
  private final WalletTransactionMapper walletTransactionMapper;

  @Override
  public WalletTransactionResponse createTransaction(TransactionRequest request) {

    WalletModel wallet =
        walletRepository
            .findByEmployee_EmployeeCode(request.getEmployeeId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Wallet", "employeeId", request.getEmployeeId()));

    WalletTransactionModel transaction = new WalletTransactionModel();
    transaction.setWallet(wallet);
    transaction.setTransactionType(request.getType());
    transaction.setAmount(request.getAmount());
    transaction.setDescription(request.getDescription());
    transaction.setTransactionDate(
        request.getTransactionDate() != null ? request.getTransactionDate() : LocalDateTime.now());

    walletTransactionRepository.save(transaction);

    if (request.getType() == TransactionType.CREDIT) {
      wallet.setBalance(wallet.getBalance().add(request.getAmount()));
    } else {
      wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
    }
    walletRepository.save(wallet);
    return walletTransactionMapper.toTransactionResponse(transaction);
  }

  @Override
  public List<WalletTransactionResponse> getTransactions(UUID walletId) {
    WalletModel wallet =
        walletRepository
            .findByUuid(walletId)
            .orElseThrow(() -> new ResourceNotFoundException("Wallet", "walletId", walletId));

    List<WalletTransactionModel> transactions = walletTransactionRepository.findByWallet(wallet);

    return transactions.stream()
        .map(
            txn ->
                new WalletTransactionResponse(
                    txn.getUuid(),
                    "TXN" + txn.getId(),
                    txn.getTransactionType().name(),
                    txn.getAmount(),
                    txn.getDescription(),
                    txn.getTransactionDate()))
        .toList();
  }
}
