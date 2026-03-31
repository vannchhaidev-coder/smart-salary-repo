package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.dto.request.TransactionRequest;
import com.vannchhai.smart_salary_api.dto.responses.PaginationDto;
import com.vannchhai.smart_salary_api.dto.responses.PaginationResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.TransactionDetailResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.WalletEmployeeTransactionResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.WalletTransactionResponse;
import com.vannchhai.smart_salary_api.enums.TransactionType;
import com.vannchhai.smart_salary_api.exceptions.ResourceNotFoundException;
import com.vannchhai.smart_salary_api.mapper.WalletTransactionMapper;
import com.vannchhai.smart_salary_api.models.WalletModel;
import com.vannchhai.smart_salary_api.models.WalletTransactionModel;
import com.vannchhai.smart_salary_api.repositories.WalletRepository;
import com.vannchhai.smart_salary_api.repositories.WalletTransactionRepository;
import com.vannchhai.smart_salary_api.services.WalletTransactionService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    transaction.setTransactionDate(LocalDateTime.now());

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
  public PaginationResponse<WalletTransactionResponse> getTransactions(
      UUID walletId, PaginationDto pagination) {

    Pageable pageable =
        PageRequest.of(
            pagination.getPage(), pagination.getSize(), Sort.by("transactionDate").descending());

    WalletModel wallet =
        walletRepository
            .findByUuid(walletId)
            .orElseThrow(() -> new ResourceNotFoundException("Wallet", "walletId", walletId));

    Page<WalletTransactionModel> pageResult =
        walletTransactionRepository.findByWallet(wallet, pageable);

    List<WalletTransactionResponse> data =
        pageResult.getContent().stream()
            .map(
                txn ->
                    new WalletTransactionResponse(
                        txn.getUuid(),
                        walletId, // ✅ now using walletId
                        "TXN" + txn.getId(),
                        txn.getTransactionType().name(),
                        txn.getAmount(),
                        txn.getDescription(),
                        txn.getTransactionDate()))
            .toList();

    pagination.setTotal(pageResult.getTotalElements());
    pagination.setTotalPages(pageResult.getTotalPages());

    return new PaginationResponse<>(data, pagination);
  }

  @Override
  public PaginationResponse<TransactionDetailResponse> getTransactionsByEmployeeId(
      UUID employeeId, PaginationDto pagination) {

    Pageable pageable =
        PageRequest.of(
            pagination.getPage(), pagination.getSize(), Sort.by("transactionDate").descending());

    WalletModel wallet =
        walletRepository
            .findByEmployeeUuid(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Wallet", "employeeId", employeeId));

    Page<WalletTransactionModel> pageResult =
        walletTransactionRepository.findByWallet(wallet, pageable);

    BigDecimal runningBalance = BigDecimal.ZERO;

    List<TransactionDetailResponse> data = new ArrayList<>();
    for (WalletTransactionModel txn : pageResult.getContent()) {

      if (txn.getTransactionType() == TransactionType.CREDIT) {
        runningBalance = runningBalance.add(txn.getAmount());
      } else {
        runningBalance = runningBalance.subtract(txn.getAmount());
      }

      data.add(
          new TransactionDetailResponse(
              txn.getUuid(),
              employeeId,
              txn.getTransactionType().name(),
              txn.getAmount(),
              txn.getTransactionDate(),
              txn.getDescription(),
              runningBalance));
    }

    // 4️⃣ Set pagination info
    pagination.setTotal(pageResult.getTotalElements());
    pagination.setTotalPages(pageResult.getTotalPages());

    return new PaginationResponse<>(data, pagination);
  }

  @Override
  public PaginationResponse<WalletEmployeeTransactionResponse> getEmployeeTransactions(
      UUID employeeId, PaginationDto paginationDto) {
    Pageable pageable =
        PageRequest.of(
            paginationDto.getPage(),
            paginationDto.getSize(),
            Sort.by("transactionDate").descending());

    Page<WalletTransactionModel> pageResult =
        walletTransactionRepository.findByWallet_Employee_Uuid(employeeId, pageable);

    List<WalletEmployeeTransactionResponse> data =
        walletTransactionMapper.toResponseList(pageResult.getContent());

    paginationDto.setTotal(pageResult.getTotalElements());
    paginationDto.setTotalPages(pageResult.getTotalPages());

    return new PaginationResponse<>(data, paginationDto);
  }
}
