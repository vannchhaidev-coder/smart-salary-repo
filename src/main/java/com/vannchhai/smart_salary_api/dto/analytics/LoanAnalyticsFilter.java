package com.vannchhai.smart_salary_api.dto.analytics;

import com.vannchhai.smart_salary_api.enums.LoanStatus;
import lombok.Data;

@Data
public class LoanAnalyticsFilter {
  private Integer year;
  private String department;
  private LoanStatus status;
}
