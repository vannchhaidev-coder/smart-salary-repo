package com.vannchhai.smart_salary_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DepartmentModel extends BaseIdModel {

  @NotBlank(message = "Department name is required")
  @Column(nullable = false)
  private String name;

  private String description;
}
