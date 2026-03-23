package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.OvertimeModel;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OvertimeRepository extends JpaRepository<OvertimeModel, UUID> {

  boolean existsByEmployeeAndWorkDate(EmployeeModel employee, LocalDate workDate);
}
