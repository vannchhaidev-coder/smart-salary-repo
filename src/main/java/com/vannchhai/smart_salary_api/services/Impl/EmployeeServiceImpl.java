package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.dto.request.EmployeeRequest;
import com.vannchhai.smart_salary_api.dto.request.EmployeeUpdateRequest;
import com.vannchhai.smart_salary_api.dto.responses.*;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeCreateResponse;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeResponse;
import com.vannchhai.smart_salary_api.dto.responses.employees.EmployeeUpdateResponse;
import com.vannchhai.smart_salary_api.exceptions.BusinessException;
import com.vannchhai.smart_salary_api.exceptions.DuplicateResourceException;
import com.vannchhai.smart_salary_api.exceptions.ResourceNotFoundException;
import com.vannchhai.smart_salary_api.mapper.EmployeeMapper;
import com.vannchhai.smart_salary_api.models.*;
import com.vannchhai.smart_salary_api.repositories.*;
import com.vannchhai.smart_salary_api.services.EmployeeService;
import com.vannchhai.smart_salary_api.spaces.EmployeeSpecification;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final EmployeeMapper employeeMapper;
  private final DepartmentRepository departmentRepository;
  private final PositionRepository positionRepository;
  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;
  private final PasswordEncoder passwordEncoder;
  private final DeductionRepository deductionRepository;
  private final SalaryRepository salaryRepository;

  @Override
  public PaginationResponse<EmployeeResponse> getListEmployee(
      String department, String position, String badge, PaginationDto pagination) {

    Pageable pageable =
        PageRequest.of(
            pagination.getPage(), pagination.getSize(), Sort.by(Sort.Direction.DESC, "id"));

    Specification<EmployeeModel> spec =
        Specification.where(EmployeeSpecification.hasDepartment(department))
            .and(EmployeeSpecification.hasPosition(position))
            .and(EmployeeSpecification.hasBadge(badge));

    Page<EmployeeModel> employee = employeeRepository.findAll(spec, pageable);
    List<EmployeeResponse> data =
        employee.getContent().stream().map(employeeMapper::toResponse).toList();

    pagination.setTotal(employee.getTotalElements());
    pagination.setTotalPages(employee.getTotalPages());

    return new PaginationResponse<>(data, pagination);
  }

  @Override
  @Transactional
  public EmployeeCreateResponse createEmployee(EmployeeRequest request) {

    userRepository
        .findByEmail(request.getEmail())
        .ifPresent(
            user -> {
              throw new DuplicateResourceException("User", "email", request.getEmail());
            });

    EmployeeModel employee = employeeMapper.toEntity(request);

    UserRoleModel employeeRole =
        userRoleRepository
            .findByRoleName("ROLE_EMPLOYEE")
            .orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", "ROLE_EMPLOYEE"));

    UserModel user =
        UserModel.builder()
            .name(request.getName())
            .email(request.getEmail())
            .passwordHash(passwordEncoder.encode("employee@123"))
            .role(employeeRole)
            .build();

    userRepository.save(user);
    employee.setUser(user);

    DepartmentModel department =
        departmentRepository
            .findByName(request.getDepartment())
            .orElseThrow(
                () -> new ResourceNotFoundException("Department", "name", request.getDepartment()));
    employee.setDepartment(department);

    PositionModel position =
        positionRepository
            .findByTitle(request.getPosition())
            .orElseThrow(
                () -> new ResourceNotFoundException("Position", "title", request.getPosition()));
    employee.setPosition(position);

    long shortTime = System.currentTimeMillis() % 1_000_000;
    employee.setEmployeeCode("EMP" + shortTime);

    EmployeeModel savedEmployee = employeeRepository.save(employee);

    SalaryModel salary =
        SalaryModel.builder()
            .employee(savedEmployee)
            .baseSalary(request.getSalary())
            .allowance(BigDecimal.ZERO)
            .effectiveDate(LocalDate.now())
            .build();

    salaryRepository.save(salary);

    return employeeMapper.toCreateResponse(savedEmployee);
  }

  @Override
  @Transactional
  public EmployeeUpdateResponse updateEmployee(UUID uuid, EmployeeUpdateRequest request) {

    EmployeeModel employee =
        employeeRepository
            .findByUuid(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "uuid", uuid));

    UserModel user = employee.getUser();

    if (!user.getEmail().equals(request.getEmail())) {
      userRepository
          .findByEmail(request.getEmail())
          .ifPresent(
              u -> {
                throw new DuplicateResourceException("User", "email", request.getEmail());
              });
    }

    user.setName(request.getName());
    user.setEmail(request.getEmail());
    userRepository.save(user);

    DepartmentModel department =
        departmentRepository
            .findByName(request.getDepartment())
            .orElseThrow(
                () -> new ResourceNotFoundException("Department", "name", request.getDepartment()));
    employee.setDepartment(department);

    PositionModel position =
        positionRepository
            .findByTitle(request.getPosition())
            .orElseThrow(
                () -> new ResourceNotFoundException("Position", "title", request.getPosition()));
    employee.setPosition(position);

    employeeMapper.updateFromRequest(request, employee);

    EmployeeModel savedEmployee = employeeRepository.save(employee);
    LocalDate today = LocalDate.now();

    Optional<SalaryModel> existingSalary =
        salaryRepository.findByEmployeeAndEffectiveDate(savedEmployee, today);

    if (existingSalary.isPresent()) {

      SalaryModel salary = existingSalary.get();
      salary.setBaseSalary(request.getSalary());
      salary.setAllowance(BigDecimal.ZERO);

      salaryRepository.save(salary);

    } else {
      SalaryModel salary =
          SalaryModel.builder()
              .employee(savedEmployee)
              .baseSalary(request.getSalary())
              .allowance(BigDecimal.ZERO)
              .effectiveDate(today)
              .build();

      salaryRepository.save(salary);
    }

    return employeeMapper.toUpdateResponse(savedEmployee);
  }

  @Override
  public EmployeeResponse getEmployeeId(UUID uuid) {
    EmployeeModel employee =
        employeeRepository
            .findByUuid(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", uuid));
    return employeeMapper.toResponse(employee);
  }

  @Override
  public void deleteEmployeeByUuid(UUID uuid) {

    EmployeeModel employee =
        employeeRepository
            .findByUuid(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "uuid", uuid));

    if (deductionRepository.existsByEmployee(employee)) {
      throw new BusinessException("Cannot delete employee because related deductions exist");
    }

    employeeRepository.delete(employee);
  }
}
