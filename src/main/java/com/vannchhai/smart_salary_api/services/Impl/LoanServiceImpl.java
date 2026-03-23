package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.dto.request.LoanRequest;
import com.vannchhai.smart_salary_api.dto.responses.PaginationDto;
import com.vannchhai.smart_salary_api.dto.responses.PaginationResponse;
import com.vannchhai.smart_salary_api.dto.responses.loans.LoanResponse;
import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.enums.ReferenceType;
import com.vannchhai.smart_salary_api.enums.TransactionType;
import com.vannchhai.smart_salary_api.exceptions.ResourceNotFoundException;
import com.vannchhai.smart_salary_api.mapper.LoanMapper;
import com.vannchhai.smart_salary_api.mapper.WalletTransactionMapper;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.models.WalletModel;
import com.vannchhai.smart_salary_api.models.WalletTransactionModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.LoanRepository;
import com.vannchhai.smart_salary_api.repositories.WalletRepository;
import com.vannchhai.smart_salary_api.repositories.WalletTransactionRepository;
import com.vannchhai.smart_salary_api.services.LoanRiskService;
import com.vannchhai.smart_salary_api.services.LoanService;
import com.vannchhai.smart_salary_api.services.WalletService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

  private final LoanRepository loanRepository;
  private final EmployeeRepository employeeRepository;
  private final LoanMapper loanMapper;
  private final WalletTransactionRepository walletTransactionRepository;
  private final WalletService walletService;
  private final LoanRiskService loanRiskService;

  @Override
  public PaginationResponse<LoanResponse> listLoans(PaginationDto pagination) {
    Pageable pageable =
        PageRequest.of(
            pagination.getPage(), pagination.getSize(), Sort.by(Sort.Direction.DESC, "id"));

    Page<LoanModel> loanPage = loanRepository.findAll(pageable);
    List<LoanResponse> data = loanPage.getContent().stream().map(loanMapper::toResponse).toList();

    pagination.setTotal(loanPage.getTotalElements());
    pagination.setTotalPages(loanPage.getTotalPages());

    return new PaginationResponse<>(data, pagination);
  }

  @Override
  public LoanResponse createLoan(LoanRequest request) {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    EmployeeModel employee =
        employeeRepository
            .findByUser_Email(username)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "name", username));

    LoanModel loan = loanMapper.toEntity(request, employee);
    loan.setRemainingBalance(loan.getAmount());
    loan = loanRepository.save(loan);

    int riskScore = loanRiskService.calculateRiskScore(loan);
    loan.setRiskScore(riskScore);
    loan.setRiskLevel(loanRiskService.getRiskLevel(riskScore));

    loan = loanRepository.save(loan);

    return loanMapper.toResponse(loan);
  }

  @Override
  public BigDecimal getTotalActiveDebt(UUID employeeId) {
    BigDecimal debt = loanRepository.getTotalActiveDebtRaw(employeeId);
    return debt != null ? debt : BigDecimal.ZERO;
  }

  @Override
  public List<LoanModel> getCompletedLoans(Long employeeId) {
    return loanRepository.findByEmployeeIdAndStatus(employeeId, LoanStatus.COMPLETED);
  }

  @Override
  public LoanModel getLoanByUuid(UUID loanId) {
    return loanRepository
        .findByUuid(loanId)
        .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", loanId));
  }

  public void disburseLoan(LoanModel loan) {
    WalletModel wallet = loan.getEmployee().getWallet();

    WalletTransactionModel tx =
        WalletTransactionModel.builder()
            .wallet(wallet)
            .transactionType(TransactionType.CREDIT)
            .amount(loan.getAmount())
            .referenceType(ReferenceType.LOAN)
            .referenceId(loan.getUuid())
            .description("Loan disbursement")
            .build();

    walletTransactionRepository.save(tx);
    walletService.credit(wallet, loan.getAmount());

    loan.setStatus(LoanStatus.APPROVED);
    loan.setRemainingBalance(loan.getAmount());

    loanRepository.save(loan);
  }

  @Override
  public void repayLoan(UUID loanId, BigDecimal amount) {

    LoanModel loan =
        loanRepository
            .findByUuid(loanId)
            .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", loanId));

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Payment must be greater than zero");
    }

    WalletModel wallet = loan.getEmployee().getWallet();
    if (wallet.getBalance().compareTo(amount) < 0) {
      throw new RuntimeException("Insufficient wallet balance");
    }

    WalletTransactionModel tx =
        WalletTransactionModel.builder()
            .wallet(wallet)
            .transactionType(TransactionType.DEBIT)
            .amount(amount)
            .referenceType(ReferenceType.LOAN_PAYMENT)
            .referenceId(loan.getUuid())
            .description("Loan repayment")
            .build();
    walletTransactionRepository.save(tx);
    walletService.debit(wallet, amount);

    loan.applyPayment(amount);

    if (loan.getRemainingBalance().compareTo(BigDecimal.ZERO) == 0) {
      loan.setStatus(LoanStatus.COMPLETED);
    }

    loanRepository.save(loan);
  }

  @Override
  public BigDecimal getTotalPaid(UUID loanId) {
    BigDecimal total = walletTransactionRepository.getTotalLoanPayment(loanId);
    return total != null ? total : BigDecimal.ZERO;
  }

  @Override
  public void approvedLoan(UUID loanId) {
    LoanModel loan =
        loanRepository
            .findByUuid(loanId)
            .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", loanId));

    disburseLoan(loan);
  }
}
