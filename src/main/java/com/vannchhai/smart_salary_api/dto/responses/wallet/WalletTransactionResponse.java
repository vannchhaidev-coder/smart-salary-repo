package com.vannchhai.smart_salary_api.dto.responses.wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletTransactionResponse {
  private UUID id;
  private UUID employeeId;
  private String transactionId;
  private String type;
  private BigDecimal amount;
  private String description;
  private LocalDateTime transactionDate;
}
