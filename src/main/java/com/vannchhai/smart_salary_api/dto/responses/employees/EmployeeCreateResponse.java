package com.vannchhai.smart_salary_api.dto.responses.employees;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateResponse {

  private String id;
  private String employeeCode;
  private String name;
  private String email;
  private String role;
  private String department;
  private String position;
  private String badge;
}
