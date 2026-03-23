package com.vannchhai.smart_salary_api.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "attendance",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "work_date"})})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceModel extends BaseIdModel {

  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "employee_id", nullable = false)
  private EmployeeModel employee;

  @Column(name = "work_date")
  private LocalDate workDate;

  @Column(name = "check_in")
  private LocalDateTime checkIn;

  @Column(name = "check_out")
  private LocalDateTime checkOut;

  @Column(name = "total_hours")
  private BigDecimal totalHours;

  @PrePersist
  public void generateUuid() {
    if (uuid == null) {
      uuid = UUID.randomUUID();
    }
  }
}
