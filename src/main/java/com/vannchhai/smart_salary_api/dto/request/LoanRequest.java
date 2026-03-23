package com.vannchhai.smart_salary_api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequest {

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "1.0", message = "Amount must be greater than 0")
  private BigDecimal amount;

  @NotBlank(message = "Reason is required")
  private String reason;

  @NotNull(message = "Repayment months is required")
  @Min(value = 1, message = "Repayment months must be at least 1")
  private Integer repaymentMonths;
}
