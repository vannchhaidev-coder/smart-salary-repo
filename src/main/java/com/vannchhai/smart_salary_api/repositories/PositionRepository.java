package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.PositionModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<PositionModel, Long> {
  Optional<PositionModel> findByTitle(String title);

  boolean existsByTitle(String title);
}
