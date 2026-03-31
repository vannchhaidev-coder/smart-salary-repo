package com.vannchhai.smart_salary_api.dto.responses.salaries;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalariesResponse {

  private String id;
  private String employeeId;
  private String employeeName;
  private String department;

  private BigDecimal baseSalary;
  private BigDecimal deductions;
  private BigDecimal netSalary;

  private String month;
  private String status;
  private LocalDate processedDate;
}
