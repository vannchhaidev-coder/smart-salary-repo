package com.vannchhai.smart_salary_api.mapper;

import com.vannchhai.smart_salary_api.dto.responses.MeResponse;
import com.vannchhai.smart_salary_api.dto.responses.login.UserDto;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {RoleMapper.class})
public interface UserMapper {

  @Mapping(target = "role", source = "user.role.roleName")
  @Mapping(
      target = "department",
      expression =
          "java(employee != null && employee.getDepartment() != null ? employee.getDepartment().getName() : null)")
  @Mapping(
      target = "employeeId",
      expression = "java(employee != null ? employee.getEmployeeCode() : null)")
  @Mapping(
      target = "position",
      expression =
          "java(employee != null && employee.getPosition() != null ? employee.getPosition().getTitle() : null)")
  @Mapping(
      target = "badge",
      expression = "java(employee != null ? employee.getBadge().name() : null)")
  @Mapping(target = "uuid", source = "user.uuid")
  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "name", source = "user.name")
  UserDto toDto(UserModel user, EmployeeModel employee);

  @Mapping(target = "uuid", expression = "java(user.getUuid().toString())")
  @Mapping(target = "role", source = "role.roleName")
  MeResponse toMeResponse(UserModel user);
}
