package com.vannchhai.smart_salary_api.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSalaryRequest {
  private UUID id;
  private String status;
}
