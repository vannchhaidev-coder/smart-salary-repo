package com.vannchhai.smart_salary_api.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class WalletBalanceResponse {
  private String employeeId;
  private BigDecimal walletBalance;
  private String currency;
}
