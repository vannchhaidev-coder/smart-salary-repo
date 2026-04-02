package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.LoanModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanModel, UUID> {

  List<LoanModel> findByEmployeeAndStatusIn(EmployeeModel employee, List<LoanStatus> statuses);

  List<LoanModel> findByEmployee_Uuid(UUID employeeUuid);

  long countByStatusIn(List<LoanStatus> statuses);

  long countByEmployee(EmployeeModel employee);

  Page<LoanModel> findByEmployee(EmployeeModel employee, Pageable pageable);

  Optional<LoanModel> findByUuid(UUID uuid);

  Optional<LoanModel> findByEmployeeId(Long employeeId);

  List<LoanModel> findByEmployeeIdAndStatus(Long employeeId, LoanStatus status);

  List<LoanModel> findByEmployeeIdAndStatusIn(Long employeeId, List<LoanStatus> statuses);

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
