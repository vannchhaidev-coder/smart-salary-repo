package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.UserModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository
    extends JpaRepository<EmployeeModel, Long>, JpaSpecificationExecutor<EmployeeModel> {

  Optional<EmployeeModel> findByEmployeeCode(String employeeCode);

  Optional<EmployeeModel> findByUser(UserModel user);

  Optional<EmployeeModel> findByUser_Email(String email);

  Optional<EmployeeModel> findByUuid(UUID uuid);

  boolean existsByEmployeeCode(String employeeCode);
}
