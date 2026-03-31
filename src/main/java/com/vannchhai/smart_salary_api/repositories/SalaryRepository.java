package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.SalaryModel;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends JpaRepository<SalaryModel, Long> {
  Optional<SalaryModel> findTopByEmployee_IdOrderByEffectiveDateDesc(Long employeeId);

  Optional<SalaryModel> findTopByEmployeeOrderByEffectiveDateDesc(EmployeeModel employee);

  boolean existsByEmployeeAndEffectiveDate(EmployeeModel employee, LocalDate effectiveDate);

  Optional<SalaryModel> findByUuid(UUID salaryId);

  Optional<SalaryModel> findByEmployeeAndEffectiveDate(EmployeeModel employee, LocalDate date);

  Optional<SalaryModel> findTopByEmployeeIdAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
      Long employeeId, LocalDate date);

  boolean existsByEmployeeIdAndEffectiveDate(Long employeeId, java.time.LocalDate effectiveDate);
}
