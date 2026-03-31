package com.vannchhai.smart_salary_api.dto.responses.salaries;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryData {
  private String id;
  private String employeeId;
  private String employeeName;
  private String department;
  private BigDecimal baseSalary;
  private BigDecimal deductions;
  private BigDecimal netSalary;
  private String month;
  private String status;
  private LocalDateTime processedDate;
  private List<DeductionItem> deductionBreakdown;
}
