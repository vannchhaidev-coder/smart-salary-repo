package com.vannchhai.smart_salary_api.dto.responses.employees;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLoanResponse {

  private String uuid;

  private String employeeId;
  private String employeeName;

  private String department;
  private BigDecimal amount;
  private String reason;
  private LocalDate requestDate;
  private String status;
  private Integer riskScore;
  private String riskLevel;
  private Integer repaymentMonths;
  private BigDecimal monthlyDeduction;
  private BigDecimal remainingBalance;
  private LocalDate approvedDate;
  private String approvedBy;
}
