package com.vannchhai.smart_salary_api.services;

import java.math.BigDecimal;
import java.util.UUID;

public interface PayrollService {

  BigDecimal getAverageSalary(UUID employeeId);

  BigDecimal getLatestSalary(UUID employeeId);
}
