package com.vannchhai.smart_salary_api.dto.request;

import com.vannchhai.smart_salary_api.enums.ReferenceType;
import com.vannchhai.smart_salary_api.enums.TransactionType;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class WalletTransactionRequest {

  private BigDecimal amount;

  private TransactionType transactionType;

  private ReferenceType referenceType;

  private UUID referenceId;

  private String description;
}
