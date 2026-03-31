package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.DeductionModel;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeductionRepository extends JpaRepository<DeductionModel, Long> {

  List<DeductionModel> findByEmployeeIdAndDeductionDate(Long employeeId, LocalDate deductionDate);

  @Query(
      """
    SELECT COALESCE(SUM(d.amount), 0)
    FROM DeductionModel d
    WHERE d.employee.id = :employeeId
      AND MONTH(d.deductionDate) = :month
      AND YEAR(d.deductionDate) = :year
""")
  Optional<BigDecimal> sumByEmployeeAndMonthAndYear(
      @Param("employeeId") Long employeeId, @Param("month") int month, @Param("year") int year);

  boolean existsByEmployee(EmployeeModel employee);
}
