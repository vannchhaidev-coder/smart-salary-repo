package com.vannchhai.smart_salary_api.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class WalletResponse {

  private UUID uuid;
  private String employeeId;
  private String name;
  private String department;
  private BigDecimal walletBalance;
  private BigDecimal baseSalary;
  private BigDecimal deductions;
  private BigDecimal netSalary;

  public WalletResponse(
          UUID uuid,
          String employeeId,
          String name,
          String department,
          BigDecimal walletBalance,
          BigDecimal baseSalary,
          BigDecimal deductions,
          BigDecimal netSalary
  ) {
    this.uuid = uuid;
    this.employeeId = employeeId;
    this.name = name;
    this.department = department;
    this.walletBalance = walletBalance;
    this.baseSalary = baseSalary;
    this.deductions = deductions;
    this.netSalary = netSalary;
  }
}