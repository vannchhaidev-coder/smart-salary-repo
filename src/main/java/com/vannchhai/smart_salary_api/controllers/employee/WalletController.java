package com.vannchhai.smart_salary_api.controllers.employee;

import com.vannchhai.smart_salary_api.dto.request.TransactionRequest;
import com.vannchhai.smart_salary_api.dto.responses.PaginationDto;
import com.vannchhai.smart_salary_api.dto.responses.PaginationResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.WalletBalanceResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.WalletResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.WalletTransactionResponse;
import com.vannchhai.smart_salary_api.services.WalletService;
import com.vannchhai.smart_salary_api.services.WalletTransactionService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(WalletController.BASE_PATH)
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYEE')")
public class WalletController {

  public static final String BASE_PATH = "/api/v1/wallet";
  private final WalletService walletService;
  private final WalletTransactionService walletTransactionService;

  @GetMapping
  public ResponseEntity<List<WalletResponse>> getAllWallets() {
    return ResponseEntity.ok(walletService.getAllWallets());
  }

  @GetMapping("/{employeeId}")
  public ResponseEntity<WalletResponse> getWallet(@PathVariable String employeeId) {
    WalletResponse wallet = walletService.getWalletByEmployeeId(employeeId);
    return ResponseEntity.ok(wallet);
  }

  @GetMapping("/{employeeId}/balance")
  public WalletBalanceResponse getWalletBalance(@PathVariable String employeeId) {
    return walletService.getWalletBalance(employeeId);
  }

  @PostMapping
  public ResponseEntity<WalletTransactionResponse> createTransaction(
      @Valid @RequestBody TransactionRequest request) {

    WalletTransactionResponse response = walletTransactionService.createTransaction(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{walletId}/transactions")
  public ResponseEntity<PaginationResponse<WalletTransactionResponse>> getWalletTransactions(
      @PathVariable UUID walletId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    PaginationDto pagination = new PaginationDto();
    pagination.setPage(page);
    pagination.setSize(size);

    PaginationResponse<WalletTransactionResponse> response =
        walletTransactionService.getTransactions(walletId, pagination);

    return ResponseEntity.ok(response);
  }
}
