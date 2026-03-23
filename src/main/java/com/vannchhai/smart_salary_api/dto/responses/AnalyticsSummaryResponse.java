package com.vannchhai.smart_salary_api.dto.responses;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsSummaryResponse {
  private long totalLoansIssued;
  private BigDecimal totalOutstanding;
  private int approvalRate;
  private int repaymentRate;
}
