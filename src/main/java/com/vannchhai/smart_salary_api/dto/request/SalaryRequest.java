package com.vannchhai.smart_salary_api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class SalaryRequest {

  @NotNull
  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal baseSalary;

  private BigDecimal allowance;

  @NotNull private LocalDate effectiveDate;
}
