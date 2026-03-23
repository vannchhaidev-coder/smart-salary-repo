package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.AttendanceModel;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceModel, UUID> {

  boolean existsByEmployee_UuidAndWorkDate(UUID employeeId, LocalDate workDate);

  long countByEmployee(EmployeeModel employee);

  @Query(
      """
        SELECT COUNT(a)
        FROM AttendanceModel a
        WHERE a.employee.uuid = :employeeId
          AND a.checkIn IS NOT NULL
          AND a.workDate BETWEEN :start AND :end
    """)
  int countPresentDaysBetween(
      @Param("employeeId") UUID employeeId,
      @Param("start") LocalDate start,
      @Param("end") LocalDate end);

  @Query(
      """
        SELECT COUNT(a)
        FROM AttendanceModel a
        WHERE a.workDate BETWEEN :start AND :end
    """)
  int countWorkingDaysBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
