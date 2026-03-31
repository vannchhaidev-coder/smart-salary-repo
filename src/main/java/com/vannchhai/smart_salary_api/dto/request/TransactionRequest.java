package com.vannchhai.smart_salary_api.dto.request;

import com.vannchhai.smart_salary_api.enums.TransactionType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

  private String employeeId;
  private TransactionType type;
  private BigDecimal amount;
  private String description;
  private LocalDateTime transactionDate;
}
