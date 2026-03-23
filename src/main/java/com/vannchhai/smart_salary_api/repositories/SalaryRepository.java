package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.SalaryModel;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends JpaRepository<SalaryModel, Long> {

  Optional<SalaryModel> findTopByEmployeeOrderByEffectiveDateDesc(EmployeeModel employee);

  Optional<SalaryModel> findByEmployeeAndEffectiveDate(EmployeeModel employee, LocalDate date);

  boolean existsByEmployeeIdAndEffectiveDate(Long employeeId, java.time.LocalDate effectiveDate);
}
