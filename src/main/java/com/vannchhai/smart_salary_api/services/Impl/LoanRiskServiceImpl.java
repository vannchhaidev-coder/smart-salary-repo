package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.services.AttendanceService;
import com.vannchhai.smart_salary_api.services.LoanRiskService;
import com.vannchhai.smart_salary_api.services.LoanService;
import com.vannchhai.smart_salary_api.services.PayrollService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class LoanRiskServiceImpl implements LoanRiskService {

  private final AttendanceService attendanceService;
  private final PayrollService payrollService;
  private final LoanService loanService;

  public LoanRiskServiceImpl(
      AttendanceService attendanceService,
      PayrollService payrollService,
      @Lazy LoanService loanService) {
    this.attendanceService = attendanceService;
    this.payrollService = payrollService;
    this.loanService = loanService;
  }

  public int calculateRiskScore(LoanModel loan) {
    int attendance = getAttendanceScore(loan);
    int repayment = getRepaymentScore(loan);
    int salary = getSalaryScore(loan);
    int debt = getDebtScore(loan);

    double score =
        (attendance * 0.40) + (repayment * 0.30) + (salary * 0.20) + ((100 - debt) * 0.10);

    return (int) Math.round(score);
  }

  public int getAttendanceScore(LoanModel loan) {
    UUID employeeId = loan.getEmployee().getUuid();

    int totalDays = attendanceService.countWorkingDaysThisMonth();
    int presentDays = attendanceService.countPresentDaysThisMonth(employeeId);

    System.out.println("Score totalDays: attendance=" + totalDays);
    System.out.println("Score presentDays: attendance=" + presentDays);
    if (totalDays == 0) return 50;
    if (presentDays == 0) return 70;

    double rate = (double) presentDays / totalDays;

    return (int) Math.round(rate * 100);
  }

  public int getRepaymentScore(LoanModel loan) {
    Long employeeId = loan.getEmployee().getId();

    List<LoanModel> pastLoans = loanService.getCompletedLoans(employeeId);

    if (pastLoans.isEmpty()) return 70;

    long onTime = pastLoans.stream().filter(l -> !l.isLate()).count();
    System.out.println("Completed loans: " + loanService.getCompletedLoans(employeeId));
    return (int) (((double) onTime / pastLoans.size()) * 100);
  }

  public int getSalaryScore(LoanModel loan) {
    UUID employeeId = loan.getEmployee().getUuid();

    BigDecimal avgSalary = payrollService.getAverageSalary(employeeId);

    if (avgSalary.compareTo(BigDecimal.valueOf(1000)) > 0) return 90;
    if (avgSalary.compareTo(BigDecimal.valueOf(500)) > 0) return 75;
    return 60;
  }

  public int getDebtScore(LoanModel loan) {
    UUID employeeId = loan.getEmployee().getUuid();

    BigDecimal totalDebt = loanService.getTotalActiveDebt(employeeId);

    totalDebt = totalDebt.add(loan.getAmount());
    BigDecimal salary = payrollService.getLatestSalary(employeeId);

    if (salary.compareTo(BigDecimal.ZERO) == 0) return 50;
    double ratio = totalDebt.divide(salary, 2, RoundingMode.HALF_UP).doubleValue();

    if (ratio < 0.3) return 20;
    if (ratio < 0.6) return 50;
    return 80;
  }

  @Override
  public String getRiskLevel(int score) {
    if (score >= 80) return "low";
    if (score >= 50) return "medium";
    return "high";
  }

  @Override
  public int countWorkingDaysThisMonth() {
    LocalDate start = LocalDate.now().withDayOfMonth(1);
    LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
    return attendanceService.countWorkingDaysBetween(start, end);
  }

  @Override
  public int countPresentDaysThisMonth(UUID employeeId) {
    LocalDate start = LocalDate.now().withDayOfMonth(1);
    LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
    return attendanceService.countPresentDaysBetween(employeeId, start, end);
  }
}
