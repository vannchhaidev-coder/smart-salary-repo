package com.vannchhai.smart_salary_api.dto.responses.loans;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanTransactionResponse {

  private String id;
  private String loanId;
  private LocalDate date;
  private String description;
  private String type;
  private BigDecimal amount;
}
