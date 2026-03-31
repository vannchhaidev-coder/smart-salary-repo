package com.vannchhai.smart_salary_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "payrolls",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "pay_month", "pay_year"})})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollModel extends BaseIdModel {

  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "employee_id", nullable = false)
  private EmployeeModel employee;

  @Column(length = 20)
  private String status;

  @Column(name = "processed_date")
  private LocalDateTime processedDate;

  @Min(1)
  @Max(12)
  @Column(name = "pay_month")
  private Integer payMonth;

  @Min(2000)
  @Column(name = "pay_year")
  private Integer payYear;

  @DecimalMin("0.0")
  @Column(name = "base_salary")
  private BigDecimal baseSalary;

  @DecimalMin("0.0")
  @Column(name = "total_allowance")
  private BigDecimal totalAllowance;

  @DecimalMin("0.0")
  @Column(name = "total_deduction")
  private BigDecimal totalDeduction;

  @DecimalMin("0.0")
  @Column(name = "net_salary")
  private BigDecimal netSalary;

  @PrePersist
  public void generateDefaults() {
    if (uuid == null) {
      uuid = UUID.randomUUID();
    }
    if (status == null) {
      status = "pending";
    }
  }
}
