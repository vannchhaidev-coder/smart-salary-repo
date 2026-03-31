package com.vannchhai.smart_salary_api.dto.responses;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessAllSalaryResponse {
  private int processed;
  private BigDecimal totalAmount;
}
