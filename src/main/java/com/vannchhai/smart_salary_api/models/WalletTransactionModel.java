package com.vannchhai.smart_salary_api.models;

import com.vannchhai.smart_salary_api.enums.ReferenceType;
import com.vannchhai.smart_salary_api.enums.TransactionType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "wallet_transactions")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionModel extends BaseIdModel {

  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "wallet_id", nullable = false)
  private WalletModel wallet;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type", nullable = false)
  private TransactionType transactionType;

  @Column(nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "reference_type")
  private ReferenceType referenceType;

  @Column(name = "reference_id")
  private UUID referenceId;

  @Column(name = "transaction_date", nullable = false)
  private LocalDateTime transactionDate;

  private String description;

  @PrePersist
  public void prePersist() {
    if (uuid == null) uuid = UUID.randomUUID();
  }
}
