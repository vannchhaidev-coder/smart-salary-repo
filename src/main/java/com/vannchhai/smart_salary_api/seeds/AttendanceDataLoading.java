package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.AttendanceModel;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.repositories.AttendanceRepository;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(8)
@Profile("dev")
public class AttendanceDataLoading implements CommandLineRunner {

  private final AttendanceRepository attendanceRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public void run(String... args) {

    List<EmployeeModel> employees = employeeRepository.findAll();

    if (employees.isEmpty()) {
      System.out.println("No employees found, skipping attendance seeding.");
      return;
    }

    LocalDate startDate = LocalDate.of(2026, 3, 1);
    LocalDate endDate = LocalDate.of(2026, 3, 31);

    for (EmployeeModel employee : employees) {
      generateAttendanceForEmployee(employee, startDate, endDate);
    }
  }

  private void generateAttendanceForEmployee(
      EmployeeModel employee, LocalDate startDate, LocalDate endDate) {

    LocalDate currentDate = startDate;

    while (!currentDate.isAfter(endDate)) {

      if (currentDate.getDayOfWeek().getValue() <= 5) {
        createAttendanceIfNotExists(employee, currentDate, 9, 17);
      }
      currentDate = currentDate.plusDays(1);
    }

    System.out.println(
        "Attendance generated for " + employee.getEmployeeCode() + " for March 2026");
  }

  private void createAttendanceIfNotExists(
      EmployeeModel employee, LocalDate date, int checkInHour, int checkOutHour) {

    if (attendanceRepository.existsByEmployee_UuidAndWorkDate(employee.getUuid(), date)) {
      return;
    }

    LocalDateTime checkIn = date.atTime(checkInHour, 0);
    LocalDateTime checkOut = date.atTime(checkOutHour, 0);
    BigDecimal totalHours = BigDecimal.valueOf(checkOutHour - checkInHour);

    AttendanceModel attendance =
        AttendanceModel.builder()
            .employee(employee)
            .workDate(date)
            .checkIn(checkIn)
            .checkOut(checkOut)
            .totalHours(totalHours)
            .build();

    attendanceRepository.save(attendance);
  }
}
