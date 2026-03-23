package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.dto.responses.WalletResponse;
import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.WalletModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<WalletModel, Long> {

  Optional<WalletModel> findByEmployee_Uuid(UUID employeeId);

  Optional<WalletModel> findByEmployee(EmployeeModel employee);

  Optional<WalletModel> findByUuid(UUID id);

  Optional<WalletModel> findByEmployee_EmployeeCode(String employeeCode);

  @Query("""
SELECT new com.vannchhai.smart_salary_api.dto.responses.WalletResponse(
    w.uuid,
    e.employeeCode,
    u.name,
    d.name,
    w.balance,
    p.baseSalary,
    COALESCE(SUM(l.remainingBalance), 0.0),
    p.baseSalary - COALESCE(SUM(l.remainingBalance), 0.0)
)
FROM WalletModel w
JOIN w.employee e
JOIN e.user u
JOIN e.department d
JOIN e.position p
LEFT JOIN LoanModel l 
    ON l.employee = e 
    AND l.status = :status
GROUP BY w.uuid, e.employeeCode, u.name, d.name, w.balance, p.baseSalary
""")
  List<WalletResponse> getAllWalletRaw(@Param("status") LoanStatus status);
}
