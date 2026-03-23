package com.vannchhai.smart_salary_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "salaries",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "effective_date"})})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryModel extends BaseIdModel {

  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "employee_id", nullable = false)
  private EmployeeModel employee;

  @DecimalMin("0.0")
  @Column(name = "base_salary")
  private BigDecimal baseSalary;

  @DecimalMin("0.0")
  private BigDecimal allowance;

  @NotNull
  @Column(name = "effective_date")
  private LocalDate effectiveDate;

  @PrePersist
  public void generateUuid() {
    if (uuid == null) {
      uuid = UUID.randomUUID();
    }
  }
}
