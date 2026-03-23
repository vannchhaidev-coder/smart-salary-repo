package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.dto.request.EmployeeRequest;
import com.vannchhai.smart_salary_api.dto.request.EmployeeUpdateRequest;
import com.vannchhai.smart_salary_api.dto.responses.*;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeCreateResponse;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeResponse;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeUpdateResponse;
import java.util.UUID;

public interface EmployeeService {

  PaginationResponse<EmployeeResponse> getListEmployee(
      String department, String position, String badge, PaginationDto pagination);

  EmployeeCreateResponse createEmployee(EmployeeRequest request);

  EmployeeUpdateResponse updateEmployee(UUID uuid, EmployeeUpdateRequest request);

  EmployeeResponse getEmployeeId(UUID uuid);

  void deleteEmployeeByUuid(UUID uuid);
}
