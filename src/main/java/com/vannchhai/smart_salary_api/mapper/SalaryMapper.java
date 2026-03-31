package com.vannchhai.smart_salary_api.mapper;

import com.vannchhai.smart_salary_api.dto.responses.salaries.DeductionItem;
import com.vannchhai.smart_salary_api.dto.responses.salaries.SalariesResponse;
import com.vannchhai.smart_salary_api.dto.responses.salaries.SalaryData;
import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.models.PayrollModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalaryMapper {

  @Mapping(target = "id", source = "uuid")
  @Mapping(target = "employeeId", source = "employee.uuid")
  @Mapping(target = "employeeName", source = "employee.user.name")
  @Mapping(
      target = "department",
      expression =
          "java(payroll.getEmployee() != null && payroll.getEmployee().getDepartment() != null ? payroll.getEmployee().getDepartment().getName() : null)")
  @Mapping(target = "deductions", source = "totalDeduction")
  @Mapping(target = "month", expression = "java(formatMonth(payroll))")
  @Mapping(target = "status", expression = "java(resolveStatus(payroll))")
  @Mapping(target = "processedDate", ignore = true)
  SalariesResponse toResponse(PayrollModel payroll);

  @Mapping(target = "id", source = "payroll.uuid")
  @Mapping(target = "employeeId", source = "payroll.employee.uuid")
  @Mapping(target = "employeeName", source = "payroll.employee.user.name")
  @Mapping(
      target = "department",
      expression =
          "java(payroll.getEmployee() != null && payroll.getEmployee().getDepartment() != null ? payroll.getEmployee().getDepartment().getName() : null)")
  @Mapping(target = "baseSalary", source = "payroll.baseSalary")
  @Mapping(target = "deductions", source = "payroll.totalDeduction")
  @Mapping(target = "netSalary", source = "payroll.netSalary")
  @Mapping(target = "month", expression = "java(formatMonth(payroll))")
  @Mapping(target = "status", expression = "java(resolveStatus(payroll))")
  @Mapping(
      target = "processedDate",
      expression =
          "java(payroll.getNetSalary() != null ? java.time.LocalDateTime.ofInstant(payroll.getUpdatedAt(), java.time.ZoneId.systemDefault()) : null)")
  @Mapping(target = "deductionBreakdown", source = "loans")
  SalaryData toSalaryData(PayrollModel payroll, List<LoanModel> loans);

  @Mapping(target = "type", constant = "loan")
  @Mapping(target = "loanId", source = "uuid")
  @Mapping(target = "amount", expression = "java(calculateMonthlyRepayment(loan))")
  @Mapping(target = "description", constant = "Monthly loan repayment")
  DeductionItem toDeductionItem(LoanModel loan);

  default String map(UUID value) {
    return value != null ? value.toString() : null;
  }

  default String formatMonth(PayrollModel p) {
    if (p.getPayYear() == null || p.getPayMonth() == null) return null;
    return String.format("%04d-%02d", p.getPayYear(), p.getPayMonth());
  }

  default String resolveStatus(PayrollModel p) {
    return p.getNetSalary() != null ? "processed" : "pending";
  }

  default BigDecimal calculateMonthlyRepayment(LoanModel loan) {
    if (loan == null) return BigDecimal.ZERO;
    int months =
        (int) java.time.temporal.ChronoUnit.MONTHS.between(loan.getStartDate(), loan.getEndDate());
    if (months <= 0) return BigDecimal.ZERO;
    BigDecimal interest =
        loan.getAmount()
            .multiply(loan.getInterestRate())
            .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
    BigDecimal total = loan.getAmount().add(interest);
    return total.divide(BigDecimal.valueOf(months), 2, java.math.RoundingMode.HALF_UP);
  }
}
