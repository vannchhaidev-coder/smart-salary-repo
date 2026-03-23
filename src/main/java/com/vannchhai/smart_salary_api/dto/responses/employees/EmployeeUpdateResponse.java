package com.vannchhai.smart_salary_api.dto.responses.employees;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeUpdateResponse {

  private UUID uuid;
  private String employeeCode;

  private String name;
  private String email;
  private String role;

  private String department;
  private String position;
}
