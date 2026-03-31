package com.vannchhai.smart_salary_api.dto.responses.salaries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryResponse {
  private boolean success;
  private SalaryData data;
}
