package com.vannchhai.smart_salary_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "positions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PositionModel extends BaseIdModel {
  @NotBlank(message = "Position title is required")
  @Column(nullable = false)
  private String title;

  @PositiveOrZero(message = "Base salary must be >= 0")
  private BigDecimal baseSalary;
}
