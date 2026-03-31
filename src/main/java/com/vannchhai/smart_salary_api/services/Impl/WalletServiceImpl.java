package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.dto.request.WalletTransactionRequest;
import com.vannchhai.smart_salary_api.dto.responses.wallet.WalletBalanceResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.WalletResponse;
import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.enums.ReferenceType;
import com.vannchhai.smart_salary_api.enums.TransactionType;
import com.vannchhai.smart_salary_api.exceptions.ResourceNotFoundException;
import com.vannchhai.smart_salary_api.mapper.LoanMapper;
import com.vannchhai.smart_salary_api.mapper.WalletMapper;
import com.vannchhai.smart_salary_api.mapper.WalletTransactionMapper;
import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.models.WalletModel;
import com.vannchhai.smart_salary_api.models.WalletTransactionModel;
import com.vannchhai.smart_salary_api.repositories.LoanRepository;
import com.vannchhai.smart_salary_api.repositories.WalletRepository;
import com.vannchhai.smart_salary_api.repositories.WalletTransactionRepository;
import com.vannchhai.smart_salary_api.services.WalletService;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;
  private final WalletTransactionRepository walletTransactionRepository;
  private final WalletTransactionMapper mapper;
  private final LoanRepository loanRepository;
  private final WalletMapper walletMapper;
  private final LoanMapper loanMapper;

  @Override
  public void credit(WalletModel wallet, BigDecimal amount) {
    wallet.setBalance(wallet.getBalance().add(amount));
  }

  @Override
  public void debit(WalletModel wallet, BigDecimal amount) {
    if (wallet.getBalance().compareTo(amount) < 0) {
      throw new RuntimeException("Insufficient balance");
    }
    wallet.setBalance(wallet.getBalance().subtract(amount));
  }

  @Override
  public void applyTransaction(
      WalletModel wallet,
      TransactionType type,
      BigDecimal amount,
      ReferenceType referenceType,
      UUID referenceId,
      String description) {
    if (type == TransactionType.CREDIT) {
      credit(wallet, amount);
    } else {
      debit(wallet, amount);
    }

    WalletTransactionModel txn =
        WalletTransactionModel.builder()
            .wallet(wallet)
            .transactionType(type)
            .amount(amount)
            .referenceType(referenceType)
            .referenceId(referenceId)
            .description(description)
            .build();

    walletTransactionRepository.save(txn);
    walletRepository.save(wallet);
  }

  @Override
  public WalletTransactionModel createTransaction(UUID id, WalletTransactionRequest request) {

    WalletModel wallet =
        walletRepository
            .findByUuid(id)
            .orElseThrow(() -> new ResourceNotFoundException("Wallet", "id", id));

    WalletTransactionModel tx = mapper.toEntity(request, wallet);

    if (request.getTransactionType() == TransactionType.CREDIT) {
      wallet.setBalance(wallet.getBalance().add(request.getAmount()));
    } else if (request.getTransactionType() == TransactionType.DEBIT) {
      wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
    }

    return walletTransactionRepository.save(tx);
  }

  @Override
  public List<WalletResponse> getAllWallets() {
    return walletRepository.getAllWalletRaw(LoanStatus.REPAYING).stream()
        .map(
            w ->
                new WalletResponse(
                    w.getUuid(),
                    w.getEmployeeId(),
                    w.getName(),
                    w.getDepartment(),
                    w.getWalletBalance(),
                    w.getBaseSalary(),
                    w.getDeductions(),
                    w.getBaseSalary().subtract(w.getDeductions())))
        .toList();
  }

  @Override
  public WalletResponse getWalletByEmployeeId(String employeeCode) {
    WalletModel wallet =
        walletRepository.findByEmployee_EmployeeCode(employeeCode).stream()
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Wallet", "Id", employeeCode));

    List<LoanModel> activeLoans =
        loanRepository.findByEmployeeIdAndStatus(wallet.getEmployee().getId(), LoanStatus.REPAYING);

    return walletMapper.toResponse(wallet, activeLoans, loanMapper);
  }

  @Override
  public WalletBalanceResponse getWalletBalance(String employeeCode) {
    WalletModel wallet =
        walletRepository.findByEmployee_EmployeeCode(employeeCode).stream()
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Wallet", "Id", employeeCode));

    return new WalletBalanceResponse(employeeCode, wallet.getBalance(), "USD");
  }
}
