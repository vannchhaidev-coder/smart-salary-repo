package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.enums.Badge;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.PayrollModel;
import com.vannchhai.smart_salary_api.models.SalaryModel;
import com.vannchhai.smart_salary_api.models.WalletModel;
import com.vannchhai.smart_salary_api.repositories.*;
import com.vannchhai.smart_salary_api.services.EmployeeDashboardService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeDashboardServiceImpl implements EmployeeDashboardService {
  private final WalletRepository walletRepository;
  private final SalaryRepository salaryRepository;
  private final AttendanceRepository attendanceRepository;
  private final LoanRepository loanRepository;
  private final PayrollRepository payrollRepository;

  public BigDecimal getSalary(EmployeeModel employee) {

    PayrollModel payroll =
        payrollRepository
            .findTopByEmployeeAndStatusOrderByPayYearDescPayMonthDesc(employee, "processed")
            .orElse(null);

    if (payroll != null) {
      return payroll.getNetSalary();
    }

    SalaryModel salary =
        salaryRepository
            .findTopByEmployee_IdOrderByEffectiveDateDesc(employee.getId())
            .orElse(null);

    return salary != null ? salary.getBaseSalary() : BigDecimal.ZERO;
  }

  public BigDecimal getWalletBalance(EmployeeModel employee) {

    WalletModel wallet = walletRepository.findByEmployee(employee).orElse(null);

    return wallet != null ? wallet.getBalance() : BigDecimal.ZERO;
  }

  @Override
  public Badge getBadge(EmployeeModel employee) {

    int risk = getRiskScore(employee);

    if (risk >= 90) return Badge.EXCELLENT;
    if (risk >= 75) return Badge.GOOD;
    if (risk >= 50) return Badge.NEEDS_IMPROVEMENT;
    return Badge.POOR;
  }

  public int getAttendanceRate(EmployeeModel employee) {

    long totalAttendance = attendanceRepository.countByEmployee(employee);
    int workingDays = 22;

    return (int) ((double) totalAttendance / workingDays * 100);
  }

  public int getFinancialHealthScore(EmployeeModel employee) {

    BigDecimal wallet = getWalletBalance(employee);
    BigDecimal salary = getSalary(employee);

    if (salary.compareTo(BigDecimal.ZERO) == 0) {
      return 0;
    }

    double ratio = wallet.doubleValue() / salary.doubleValue();

    if (ratio >= 1) return 90;
    if (ratio >= 0.7) return 80;
    if (ratio >= 0.5) return 70;
    if (ratio >= 0.3) return 60;

    return 50;
  }

  public int getRiskScore(EmployeeModel employee) {

    int attendance = getAttendanceRate(employee);
    int health = getFinancialHealthScore(employee);

    long activeLoans = loanRepository.countByEmployee(employee);

    int loanPenalty = (int) (activeLoans * 5);
    int score = (attendance + health) / 2 - loanPenalty;
    return Math.max(score, 0);
  }
}
