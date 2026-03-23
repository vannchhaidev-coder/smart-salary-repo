package com.vannchhai.smart_salary_api.controllers.employee;

import com.vannchhai.smart_salary_api.dto.request.LoanRequest;
import com.vannchhai.smart_salary_api.dto.request.WalletTransactionRequest;
import com.vannchhai.smart_salary_api.dto.responses.loans.LoanResponse;
import com.vannchhai.smart_salary_api.dto.responses.loans.LoanTransactionResponse;
import com.vannchhai.smart_salary_api.mapper.WalletTransactionMapper;
import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.services.LoanService;
import com.vannchhai.smart_salary_api.services.LoanTransactionService;
import com.vannchhai.smart_salary_api.services.WalletService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LoanController.BASE_PATH)
@RequiredArgsConstructor
public class LoanController {

  public static final String BASE_PATH = "/api/v1/loans";
  private final LoanService loanService;
  private final LoanTransactionService loanTransactionService;
  private final WalletTransactionMapper walletTransactionMapper;
  private final WalletService walletService;

  @PostMapping
  public LoanResponse createLoan(@Valid @RequestBody LoanRequest request) {
    return loanService.createLoan(request);
  }

  @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
  @GetMapping("/{loanId}/transactions")
  public ResponseEntity<List<LoanTransactionResponse>> getLoanTransactions(
      @PathVariable UUID loanId) {
    LoanModel loan = loanService.getLoanByUuid(loanId);

    if (isEmployee() && !isOwner(loan)) {
      throw new AccessDeniedException("Forbidden");
    }

    List<LoanTransactionResponse> response =
        loanTransactionService.getLoanTransactions(loanId).stream()
            .map(walletTransactionMapper::toLoanDto)
            .toList();

    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasRole('EMPLOYEE')")
  @PostMapping("/{loanId}/transactions")
  public ResponseEntity<?> createLoanTransaction(
      @PathVariable UUID loanId, @Valid @RequestBody WalletTransactionRequest request) {

    walletService.createTransaction(loanId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully");
  }

  @PreAuthorize("hasRole('EMPLOYEE')")
  @PostMapping("/{uuid}/repay")
  public ResponseEntity<?> repayLoan(@PathVariable UUID uuid, @RequestParam BigDecimal amount) {
    loanService.repayLoan(uuid, amount);
    return ResponseEntity.ok("Payment successful");
  }

  private boolean isEmployee() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));
  }

  private boolean isOwner(LoanModel loan) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return loan.getEmployee().getUser().getEmail().equals(username);
  }
}
