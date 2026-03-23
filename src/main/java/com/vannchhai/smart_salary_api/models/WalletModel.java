package com.vannchhai.smart_salary_api.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "wallets",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id"})})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WalletModel extends BaseIdModel {

  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @OneToOne
  @JoinColumn(name = "employee_id", nullable = false, unique = true)
  private EmployeeModel employee;

  @Column(nullable = false)
  private BigDecimal balance;

  @PrePersist
  public void prePersist() {
    if (uuid == null) uuid = UUID.randomUUID();
  }
}
