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
    name = "overtime",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "work_date"})})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeModel extends BaseIdModel {

  @Column(nullable = false, unique = true)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "employee_id", nullable = false)
  @NotNull(message = "Employee is required")
  private EmployeeModel employee;

  @NotNull(message = "Work date is required")
  private LocalDate workDate;

  @NotNull(message = "Hours is required")
  @DecimalMin(value = "0.0", message = "Hours must be positive")
  private BigDecimal hours;

  @NotNull(message = "Rate is required")
  @DecimalMin(value = "0.0", message = "Rate must be positive")
  private BigDecimal rate;

  @PrePersist
  public void generateUuid() {
    if (uuid == null) {
      uuid = UUID.randomUUID();
    }
  }
}
