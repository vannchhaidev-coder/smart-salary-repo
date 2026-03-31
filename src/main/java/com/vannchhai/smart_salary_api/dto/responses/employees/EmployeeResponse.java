package com.vannchhai.smart_salary_api.dto.responses.employees;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

  private String id;
  private String employeeId;
  private String name;
  private String email;
  private String role;
  private String department;
  private String position;
  private BigDecimal salary;
  private BigDecimal walletBalance;
  private Integer financialHealthScore;
  private Integer attendanceRate;
  private Integer riskScore;
  private String badge;
}
