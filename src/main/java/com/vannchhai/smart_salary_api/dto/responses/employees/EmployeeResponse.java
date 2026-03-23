package com.vannchhai.smart_salary_api.dto.responses.employees;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

  private String id;
  private String employeeCode;
  private String name;
  private String email;
  private String role;
  private String department;
  private String position;
  private LocalDate joinDate;
  private BigDecimal salary;
  private BigDecimal walletBalance;
  private Integer financialHealthScore;
  private Integer attendanceRate;
  private Integer riskScore;
  private String badge;
}
