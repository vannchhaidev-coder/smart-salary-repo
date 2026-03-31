package com.vannchhai.smart_salary_api.controllers.admin;

import com.vannchhai.smart_salary_api.dto.request.EmployeeRequest;
import com.vannchhai.smart_salary_api.dto.request.EmployeeUpdateRequest;
import com.vannchhai.smart_salary_api.dto.responses.*;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeCreateResponse;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeResponse;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeUpdateResponse;
import com.vannchhai.smart_salary_api.services.EmployeeService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EmployeeController.BASE_PATH)
public class EmployeeController {

  public static final String BASE_PATH = "/api/v1/employees";
  private final EmployeeService employeeService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public PaginationResponse<EmployeeResponse> getEmployees(
      @RequestParam(required = false) String department,
      @RequestParam(required = false) String position,
      @RequestParam(required = false) String badge,
      @Valid PaginationDto pagination) {
    return employeeService.getListEmployee(department, position, badge, pagination);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public EmployeeCreateResponse createEmployee(@Valid @RequestBody EmployeeRequest request) {
    return employeeService.createEmployee(request);
  }

  @GetMapping("/{uuid}")
  @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
  public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable UUID uuid) {
    return ResponseEntity.ok(employeeService.getEmployeeId(uuid));
  }

  @PutMapping("/{uuid}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<EmployeeUpdateResponse> updateEmployee(
      @PathVariable UUID uuid, @Valid @RequestBody EmployeeUpdateRequest request) {

    return ResponseEntity.ok(employeeService.updateEmployee(uuid, request));
  }

  @DeleteMapping("/{uuid}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteEmployee(@PathVariable UUID uuid) {
    employeeService.deleteEmployeeByUuid(uuid);
    return ResponseEntity.noContent().build();
  }
}
