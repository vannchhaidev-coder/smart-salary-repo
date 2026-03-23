package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.DepartmentModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentModel, Long> {
  Optional<DepartmentModel> findByName(String name);

  boolean existsByName(String name);
}
