package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.PayrollModel;
import com.vannchhai.smart_salary_api.models.SalaryModel;
import com.vannchhai.smart_salary_api.repositories.DeductionRepository;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.PayrollRepository;
import com.vannchhai.smart_salary_api.repositories.SalaryRepository;
import com.vannchhai.smart_salary_api.services.PayrollService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
  private final PayrollRepository payrollRepository;
  private final EmployeeRepository employeeRepository;
  private final SalaryRepository salaryRepository;
  private final DeductionRepository deductionRepository;

  public BigDecimal getLatestSalary(UUID employeeId) {
    BigDecimal latest = payrollRepository.getLatestSalaryRaw(employeeId);
    return latest != null ? latest : BigDecimal.ZERO;
  }

  public BigDecimal getAverageSalary(UUID employeeId) {
    Double avg = payrollRepository.getAverageSalaryRaw(employeeId);
    return avg != null ? BigDecimal.valueOf(avg) : BigDecimal.ZERO;
  }

  @Override
  public void generatePayroll(int month, int year) {
    List<EmployeeModel> employees = employeeRepository.findAll();

    LocalDate payrollDate = LocalDate.of(year, month, 1);

    for (EmployeeModel employee : employees) {

      boolean exists =
          payrollRepository.existsByEmployeeIdAndPayMonthAndPayYear(employee.getId(), month, year);

      if (exists) {
        continue;
      }

      SalaryModel salary =
          salaryRepository
              .findTopByEmployeeIdAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                  employee.getId(), payrollDate)
              .orElseThrow(
                  () ->
                      new RuntimeException(
                          "Salary not configured for employee: " + employee.getUuid()));

      BigDecimal baseSalary =
          salary.getBaseSalary() != null ? salary.getBaseSalary() : BigDecimal.ZERO;

      BigDecimal allowance =
          salary.getAllowance() != null ? salary.getAllowance() : BigDecimal.ZERO;

      BigDecimal totalDeduction =
          deductionRepository
              .sumByEmployeeAndMonthAndYear(employee.getId(), month, year)
              .orElse(BigDecimal.ZERO);

      BigDecimal netSalary = baseSalary.add(allowance).subtract(totalDeduction);

      PayrollModel payroll =
          PayrollModel.builder()
              .employee(employee)
              .payMonth(month)
              .payYear(year)
              .baseSalary(baseSalary)
              .totalAllowance(allowance)
              .totalDeduction(totalDeduction)
              .netSalary(netSalary)
              .status("processed")
              .processedDate(LocalDateTime.now())
              .build();

      payrollRepository.save(payroll);
    }
  }
}
