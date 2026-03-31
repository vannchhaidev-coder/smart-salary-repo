package com.vannchhai.smart_salary_api.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSalaryRequest {
  private String employeeId;
  private BigDecimal baseSalary;
  private BigDecimal deductions;
  private String month;
}
