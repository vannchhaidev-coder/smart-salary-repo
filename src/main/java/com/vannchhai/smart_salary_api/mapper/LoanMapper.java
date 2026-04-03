package com.vannchhai.smart_salary_api.mapper;

import com.vannchhai.smart_salary_api.dto.request.LoanRequest;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeLoanResponse;
import com.vannchhai.smart_salary_api.dto.responses.loans.LoanResponse;
import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.LoanModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    builder = @org.mapstruct.Builder(disableBuilder = true),
    imports = {
      LoanStatus.class,
      java.time.LocalDate.class,
      java.math.BigDecimal.class,
      java.math.RoundingMode.class
    })
public interface LoanMapper {

  @Mapping(target = "uuid", source = "uuid")
  @Mapping(target = "employeeName", source = "employee.user.name")
  @Mapping(target = "department", source = "employee.department.name")
  @Mapping(target = "amount", source = "amount")
  @Mapping(target = "reason", source = "reason")
  @Mapping(target = "requestDate", expression = "java(loan.getCreatedAt().toString())")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "repaymentMonths", expression = "java(getMonths(loan))")
  @Mapping(target = "monthlyDeduction", expression = "java(calculateMonthly(loan))")
  @Mapping(target = "remainingBalance", source = "remainingBalance")
  @Mapping(target = "riskScore", source = "riskScore")
  @Mapping(target = "riskLevel", source = "riskLevel")
  LoanResponse toResponse(LoanModel loan);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "employee", source = "employee")
  @Mapping(target = "status", expression = "java(LoanStatus.PENDING)")
  @Mapping(target = "startDate", expression = "java(LocalDate.now())")
  @Mapping(
      target = "endDate",
      expression = "java(LocalDate.now().plusMonths(request.getRepaymentMonths()))")
  @Mapping(target = "interestRate", constant = "5.0")
  LoanModel toEntity(LoanRequest request, EmployeeModel employee);

  @Mapping(target = "uuid", source = "uuid")
  @Mapping(
      target = "employeeId",
      expression =
          "java(loan.getEmployee() != null ? loan.getEmployee().getUuid().toString() : null)")
  @Mapping(
      target = "employeeName",
      expression =
          "java(loan.getEmployee() != null && loan.getEmployee().getUser() != null ? loan.getEmployee().getUser().getName() : null)")
  @Mapping(
      target = "department",
      expression =
          "java(loan.getEmployee() != null && loan.getEmployee().getDepartment() != null ? loan.getEmployee().getDepartment().getName() : null)")
  @Mapping(target = "amount", source = "amount")
  @Mapping(target = "reason", source = "reason")
  @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
  @Mapping(target = "repaymentMonths", expression = "java(getMonths(loan))")
  @Mapping(target = "monthlyDeduction", expression = "java(calculateMonthly(loan))")
  @Mapping(target = "remainingBalance", source = "remainingBalance")
  @Mapping(target = "riskScore", source = "riskScore")
  @Mapping(target = "riskLevel", source = "riskLevel")
  @Mapping(
      target = "requestDate",
      expression =
          "java(loan.getCreatedAt() != null ? loan.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null)")
  @Mapping(
      target = "approvedDate",
      expression = "java(loan.getEndDate() != null ? loan.getEndDate() : null)")
  @Mapping(target = "approvedBy", source = "approvedBy")
  EmployeeLoanResponse toResponseEmployeeList(LoanModel loan);

  @Named("mapStatus")
  default String mapStatus(LoanStatus status) {
    return status == null ? null : status.name().toLowerCase();
  }

  default int getMonths(LoanModel loan) {
    if (loan.getStartDate() == null || loan.getEndDate() == null) return 0;
    return (int)
        java.time.temporal.ChronoUnit.MONTHS.between(loan.getStartDate(), loan.getEndDate());
  }

  default BigDecimal calculateMonthly(LoanModel loan) {
    int months = getMonths(loan);
    if (months <= 0) return BigDecimal.ZERO;

    BigDecimal interest =
        loan.getAmount()
            .multiply(loan.getInterestRate())
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

    BigDecimal total = loan.getAmount().add(interest);
    return total.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
  }
}
