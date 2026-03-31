package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.PayrollModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollModel, Long> {

  Optional<PayrollModel> findTopByEmployeeAndStatusOrderByPayYearDescPayMonthDesc(
      EmployeeModel employee, String status);

  Optional<PayrollModel> findByUuid(UUID salaryId);

  List<PayrollModel> findByStatus(String status);

  boolean existsByEmployeeIdAndPayMonthAndPayYear(
      Long employeeId, Integer payMonth, Integer payYear);

  @Query(
      """
        SELECT p.netSalary
        FROM PayrollModel p
        WHERE p.employee.uuid = :employeeId
        ORDER BY p.payYear DESC, p.payMonth DESC
    """)
  BigDecimal getLatestSalaryRaw(@Param("employeeId") UUID employeeId);

  @Query(
      """
  SELECT AVG(p.netSalary)
  FROM PayrollModel p
  WHERE p.employee.uuid = :employeeId
""")
  Double getAverageSalaryRaw(@Param("employeeId") UUID employeeId);
}
