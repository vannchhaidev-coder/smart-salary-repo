package com.vannchhai.smart_salary_api.mapper;

import com.vannchhai.smart_salary_api.dto.request.EmployeeRequest;
import com.vannchhai.smart_salary_api.dto.request.EmployeeUpdateRequest;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeCreateResponse;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeResponse;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeUpdateResponse;
import com.vannchhai.smart_salary_api.enums.Badge;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.services.EmployeeDashboardService;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    uses = {RoleMapper.class},
    builder = @Builder(disableBuilder = true))
public abstract class EmployeeMapper {

  @Autowired protected EmployeeDashboardService dashboardService;

  @Mapping(target = "id", source = "uuid")
  @Mapping(target = "employeeId", source = "employeeCode")
  @Mapping(target = "name", source = "user.name")
  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "role", source = "user.role.roleName")
  @Mapping(target = "department", source = "department.name")
  @Mapping(target = "position", source = "position.title")
  @Mapping(target = "salary", expression = "java(dashboardService.getSalary(employee))")
  @Mapping(
      target = "walletBalance",
      expression = "java(dashboardService.getWalletBalance(employee))")
  @Mapping(
      target = "attendanceRate",
      expression = "java(dashboardService.getAttendanceRate(employee))")
  @Mapping(
      target = "financialHealthScore",
      expression = "java(dashboardService.getFinancialHealthScore(employee))")
  @Mapping(target = "riskScore", expression = "java(dashboardService.getRiskScore(employee))")
  @Mapping(target = "badge", expression = "java(dashboardService.getBadge(employee).name())")
  public abstract EmployeeResponse toResponse(EmployeeModel employee);

  @Mapping(target = "department", ignore = true)
  @Mapping(target = "position", ignore = true)
  @Mapping(target = "user", ignore = true)
  public abstract EmployeeModel toEntity(EmployeeRequest employee);

  @Mapping(target = "name", source = "user.name")
  @Mapping(target = "email", source = "user.email")
  @Mapping(
      target = "role",
      expression = "java(roleMapper.normalize(employee.getUser().getRole().getRoleName()))")
  @Mapping(target = "department", source = "department.name")
  @Mapping(target = "position", source = "position.title")
  public abstract EmployeeCreateResponse toCreateResponse(EmployeeModel employee);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "employeeCode", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "department", ignore = true)
  @Mapping(target = "position", ignore = true)
  public abstract void updateFromRequest(
      EmployeeUpdateRequest request, @MappingTarget EmployeeModel employee);

  @Mapping(target = "uuid", source = "uuid")
  @Mapping(target = "employeeCode", source = "employeeCode")
  @Mapping(target = "name", source = "user.name")
  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "role", source = "user.role.roleName")
  @Mapping(target = "department", source = "department.name")
  @Mapping(target = "position", source = "position.title")
  public abstract EmployeeUpdateResponse toUpdateResponse(EmployeeModel employee);

  protected String mapStatus(Badge status) {
    if (status == null) return null;

    return Arrays.stream(status.name().toLowerCase().split("_"))
        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
        .collect(Collectors.joining(" "));
  }
}
