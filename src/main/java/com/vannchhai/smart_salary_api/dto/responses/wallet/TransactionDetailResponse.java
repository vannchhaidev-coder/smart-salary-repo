package com.vannchhai.smart_salary_api.dto.responses.wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailResponse {
  private UUID id;

  private UUID employeeId;

  private String type;

  private BigDecimal amount;

  private LocalDateTime date;

  private String description;

  private BigDecimal balanceAfter;
}
