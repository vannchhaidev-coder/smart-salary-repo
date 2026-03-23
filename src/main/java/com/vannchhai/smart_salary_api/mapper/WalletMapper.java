package com.vannchhai.smart_salary_api.mapper;

import com.vannchhai.smart_salary_api.dto.responses.WalletResponse;
import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.models.WalletModel;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = LoanMapper.class)
public interface WalletMapper {
  @Mapping(target = "uuid", source = "wallet.uuid")
  @Mapping(target = "employeeId", source = "wallet.employee.employeeCode")
  @Mapping(target = "name", source = "wallet.employee.user.name")
  @Mapping(target = "department", source = "wallet.employee.department.name")
  @Mapping(target = "walletBalance", source = "wallet.balance")
  @Mapping(target = "baseSalary", source = "wallet.employee.position.baseSalary")
  @Mapping(
      target = "deductions",
      expression = "java(calculateTotalDeductions(loans, loanMapper))")
  @Mapping(
      target = "netSalary",
      expression =
          "java(wallet.getEmployee().getPosition().getBaseSalary().subtract(calculateTotalDeductions(loans, loanMapper)))")
  WalletResponse toResponse(
      WalletModel wallet, List<LoanModel> loans, @Context LoanMapper loanMapper);

  default BigDecimal calculateTotalDeductions(
      List<LoanModel> loans, @Context LoanMapper loanMapper) {
    if (loans == null || loans.isEmpty()) return BigDecimal.ZERO;
    return loans.stream()
        .map(loanMapper::calculateMonthly)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
