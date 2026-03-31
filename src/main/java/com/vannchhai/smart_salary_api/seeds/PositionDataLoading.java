package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.PositionModel;
import com.vannchhai.smart_salary_api.repositories.PositionRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(4)
public class PositionDataLoading implements CommandLineRunner {

  private final PositionRepository positionRepository;

  @Override
  public void run(String... args) {

    createPositionIfNotExists("Software Engineer", new BigDecimal("1500"));
    createPositionIfNotExists("HR Manager", new BigDecimal("1200"));
    createPositionIfNotExists("Accountant", new BigDecimal("1000"));
    createPositionIfNotExists("Manager", new BigDecimal("2000"));

    createPositionIfNotExists("Sales Executive", new BigDecimal("1300"));
    createPositionIfNotExists("Operations Lead", new BigDecimal("1800"));
    createPositionIfNotExists("Research Analyst", new BigDecimal("1600"));
    createPositionIfNotExists("Marketing Specialist", new BigDecimal("1400"));
  }

  private void createPositionIfNotExists(String title, BigDecimal salary) {

    if (positionRepository.existsByTitle(title)) {
      return;
    }

    positionRepository.save(PositionModel.builder().title(title).baseSalary(salary).build());
  }
}
