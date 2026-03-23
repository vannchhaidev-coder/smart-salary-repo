package com.vannchhai.smart_salary_api.dto.analytics;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyLoanDto {
  private String month;
  private BigDecimal amount;
  private long count;
}
