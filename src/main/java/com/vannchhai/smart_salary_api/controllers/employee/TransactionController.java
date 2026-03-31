package com.vannchhai.smart_salary_api.controllers.employee;

import com.vannchhai.smart_salary_api.dto.responses.PaginationDto;
import com.vannchhai.smart_salary_api.dto.responses.PaginationResponse;
import com.vannchhai.smart_salary_api.dto.responses.wallet.TransactionDetailResponse;
import com.vannchhai.smart_salary_api.services.WalletTransactionService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TransactionController.BASE_PATH)
@RequiredArgsConstructor
public class TransactionController {
  public static final String BASE_PATH = "/api/v1/transactions";

  private final WalletTransactionService walletTransactionService;

  @PreAuthorize("hasRole('EMPLOYEE')")
  @GetMapping("/employee/{employeeId}")
  public ResponseEntity<PaginationResponse<TransactionDetailResponse>> getByEmployeeId(
      @PathVariable UUID employeeId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    PaginationDto pagination = new PaginationDto();
    pagination.setPage(page);
    pagination.setSize(size);

    return ResponseEntity.ok(
        walletTransactionService.getTransactionsByEmployeeId(employeeId, pagination));
  }
}
