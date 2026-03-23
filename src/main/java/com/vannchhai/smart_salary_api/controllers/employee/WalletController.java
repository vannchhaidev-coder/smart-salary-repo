package com.vannchhai.smart_salary_api.controllers.employee;

import com.vannchhai.smart_salary_api.dto.request.TransactionRequest;
import com.vannchhai.smart_salary_api.dto.request.WalletTransactionRequest;
import com.vannchhai.smart_salary_api.dto.responses.WalletBalanceResponse;
import com.vannchhai.smart_salary_api.dto.responses.WalletResponse;
import com.vannchhai.smart_salary_api.dto.responses.WalletTransactionResponse;
import com.vannchhai.smart_salary_api.services.WalletService;
import com.vannchhai.smart_salary_api.services.WalletTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(WalletController.BASE_PATH)
@RequiredArgsConstructor
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
  public List<WalletTransactionResponse> getTransactions(
          @PathVariable UUID walletId) {
    return walletTransactionService.getTransactions(walletId);
  }
}
