package com.vannchhai.smart_salary_api.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskDistributionDto {
  private String level;
  private long count;
}
