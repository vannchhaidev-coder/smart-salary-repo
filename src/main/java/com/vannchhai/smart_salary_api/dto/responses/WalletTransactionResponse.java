package com.vannchhai.smart_salary_api.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WalletTransactionResponse {
    private UUID id;
    private String transactionId;
    private String type;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
}
