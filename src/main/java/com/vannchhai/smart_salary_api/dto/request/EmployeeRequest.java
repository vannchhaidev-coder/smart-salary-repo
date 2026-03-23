package com.vannchhai.smart_salary_api.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {

  @NotBlank(message = "Name is required")
  private String name;

  @Email(message = "Email must be valid")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Department is required")
  private String department;

  @NotBlank(message = "Position is required")
  private String position;

  @NotNull(message = "Salary is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
  private BigDecimal salary;
}
