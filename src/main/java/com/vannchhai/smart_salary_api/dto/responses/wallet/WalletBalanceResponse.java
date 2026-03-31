package com.vannchhai.smart_salary_api.dto.responses.wallet;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletBalanceResponse {
  private String employeeId;
  private BigDecimal walletBalance;
  private String currency;
}
