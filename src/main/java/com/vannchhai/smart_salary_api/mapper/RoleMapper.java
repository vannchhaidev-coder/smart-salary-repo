package com.vannchhai.smart_salary_api.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  default String normalize(String roleName) {
    if (roleName == null) {
      return null;
    }
    return roleName.startsWith("ROLE_") ? roleName.substring(5) : roleName;
  }
}
