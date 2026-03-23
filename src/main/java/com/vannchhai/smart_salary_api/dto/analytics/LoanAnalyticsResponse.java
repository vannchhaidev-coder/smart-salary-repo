package com.vannchhai.smart_salary_api.dto.analytics;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanAnalyticsResponse {
  private List<MonthlyLoanDto> monthlyLoanVolume;
  private List<RiskDistributionDto> riskDistribution;
  private List<DepartmentLoanDto> departmentLoans;
}
