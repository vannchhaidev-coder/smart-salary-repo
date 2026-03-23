package com.vannchhai.smart_salary_api.dto.responses.loans;

import java.math.BigDecimal;
import java.util.UUID;

import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.services.LoanService;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponse {

  private UUID uuid;

  private String employeeName;

  private String department;

  private Double amount;

  private String reason;

  private String requestDate;

  private LoanStatus status;

  private Integer riskScore;

  private String riskLevel;

  private Integer repaymentMonths;

  private BigDecimal monthlyDeduction;

  private BigDecimal remainingBalance;
}
