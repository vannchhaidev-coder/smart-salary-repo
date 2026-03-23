package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.repositories.PayrollRepository;
import com.vannchhai.smart_salary_api.services.PayrollService;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
  private final PayrollRepository payrollRepository;

  public BigDecimal getLatestSalary(UUID employeeId) {
    BigDecimal latest = payrollRepository.getLatestSalaryRaw(employeeId);
    return latest != null ? latest : BigDecimal.ZERO;
  }

  public BigDecimal getAverageSalary(UUID employeeId) {
    Double avg = payrollRepository.getAverageSalaryRaw(employeeId);
    return avg != null ? BigDecimal.valueOf(avg) : BigDecimal.ZERO;
  }
}
