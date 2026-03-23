package com.vannchhai.smart_salary_api.controllers.admin;

import com.vannchhai.smart_salary_api.dto.responses.PaginationDto;
import com.vannchhai.smart_salary_api.dto.responses.PaginationResponse;
import com.vannchhai.smart_salary_api.dto.responses.loans.LoanResponse;
import com.vannchhai.smart_salary_api.services.LoanService;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AdminLoanController.BASE_PATH)
@RequiredArgsConstructor
public class AdminLoanController {

  public static final String BASE_PATH = "/api/v1/loans";
  private final LoanService loanService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<PaginationResponse<LoanResponse>> listLoans(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

    PaginationDto pagination = new PaginationDto();
    pagination.setPage(page);
    pagination.setSize(size);

    PaginationResponse<LoanResponse> response = loanService.listLoans(pagination);
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}/approve")
  public ResponseEntity<String> approveLoan(@PathVariable UUID id) {
    loanService.approvedLoan(id);
    return ResponseEntity.ok("Loan approved successfully");
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/employees/{id}/debt")
  public ResponseEntity<BigDecimal> getEmployeeDebt(@PathVariable UUID id) {
    BigDecimal debt = loanService.getTotalActiveDebt(id);
    return ResponseEntity.ok(debt);
  }
}
