package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.enums.Badge;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import java.math.BigDecimal;

public interface EmployeeDashboardService {

  BigDecimal getSalary(EmployeeModel employee);

  BigDecimal getWalletBalance(EmployeeModel employee);

  Badge getBadge(EmployeeModel employee);

  int getAttendanceRate(EmployeeModel employee);

  int getFinancialHealthScore(EmployeeModel employee);

  int getRiskScore(EmployeeModel employee);
}
