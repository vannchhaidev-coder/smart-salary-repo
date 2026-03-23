package com.vannchhai.smart_salary_api.models;

import com.vannchhai.smart_salary_api.enums.DeductionType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "deductions")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DeductionModel extends BaseIdModel {

  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "employee_id", nullable = false)
  private EmployeeModel employee;

  @Enumerated(EnumType.STRING)
  @Column(name = "deduction_type", nullable = false)
  private DeductionType deductionType;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(name = "deduction_date", nullable = false)
  private LocalDate deductionDate;

  @PrePersist
  public void prePersist() {
    if (uuid == null) uuid = UUID.randomUUID();
  }
}
