package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.LoanModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanModel, UUID> {

  long countByEmployee(EmployeeModel employee);

  Optional<LoanModel> findByUuid(UUID uuid);

  List<LoanModel> findByEmployeeIdAndStatus(Long employeeId, LoanStatus status);

  @Query(
      """
        SELECT COALESCE(SUM(l.amount), 0)
        FROM LoanModel l
        WHERE l.employee.uuid = :employeeId
        AND l.status IN ('PENDING','APPROVED')
    """)
  BigDecimal getTotalActiveDebtRaw(@Param("employeeId") UUID employeeId);

  long countByStatus(LoanStatus status);

  @Query("SELECT COUNT(l) FROM LoanModel l")
  long countAllLoans();

  @Query(
      """
        SELECT COALESCE(SUM(l.remainingBalance), 0)
        FROM LoanModel l
        WHERE l.status != 'REJECTED'
    """)
  BigDecimal getTotalOutstanding();
}
