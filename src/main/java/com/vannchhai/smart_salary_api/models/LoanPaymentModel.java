package com.vannchhai.smart_salary_api.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "loan_payments")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LoanPaymentModel extends BaseIdModel {

  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "loan_id", nullable = false)
  private LoanModel loan;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(name = "payment_date", nullable = false)
  private LocalDate paymentDate;

  @PrePersist
  public void prePersist() {
    if (uuid == null) uuid = UUID.randomUUID();
  }
}
