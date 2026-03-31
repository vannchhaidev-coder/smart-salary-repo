package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.dto.request.LoanRequest;
import com.vannchhai.smart_salary_api.dto.responses.PaginationDto;
import com.vannchhai.smart_salary_api.dto.responses.PaginationResponse;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeLoanResponse;
import com.vannchhai.smart_salary_api.dto.responses.loans.LoanResponse;
import com.vannchhai.smart_salary_api.models.LoanModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface LoanService {
  PaginationResponse<LoanResponse> listLoans(PaginationDto pagination);

  PaginationResponse<EmployeeLoanResponse> getLoanEmployeeList(PaginationDto pagination);

  LoanResponse createLoan(LoanRequest request);

  BigDecimal getTotalActiveDebt(UUID employeeId);

  List<LoanModel> getCompletedLoans(Long employeeId);

  LoanModel getLoanByUuid(UUID loanId);

  BigDecimal getTotalPaid(UUID loanId);

  LoanModel disburseLoan(LoanModel loan);

  void repayLoan(UUID loanId, BigDecimal amount);

  EmployeeLoanResponse approvedLoan(UUID loanId);

  EmployeeLoanResponse rejectLoan(UUID loanId);
}
