package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.models.LoanModel;
import java.util.UUID;

public interface LoanRiskService {

  int calculateRiskScore(LoanModel loan);

  int getAttendanceScore(LoanModel loan);

  int getRepaymentScore(LoanModel loan);

  int getSalaryScore(LoanModel loan);

  int getDebtScore(LoanModel loan);

  String getRiskLevel(int score);

  int countWorkingDaysThisMonth();

  int countPresentDaysThisMonth(UUID employeeId);
}
