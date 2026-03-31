package com.vannchhai.smart_salary_api.dto.responses.salaries;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductionItem {
  private String type;
  private String loanId;
  private BigDecimal amount;
  private String description;
}
