package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.DeductionModel;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeductionRepository extends JpaRepository<DeductionModel, Long> {

  List<DeductionModel> findByEmployeeIdAndDeductionDate(Long employeeId, LocalDate deductionDate);

  boolean existsByEmployee(EmployeeModel employee);
}
