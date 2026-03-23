package com.vannchhai.smart_salary_api.seeds;

import com.vannchhai.smart_salary_api.models.AttendanceModel;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.repositories.AttendanceRepository;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(8)
public class AttendanceDataLoading implements CommandLineRunner {

  private final AttendanceRepository attendanceRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public void run(String... args) {

    EmployeeModel emp1 = employeeRepository.findByEmployeeCode("EMP001").orElseThrow();

    EmployeeModel emp2 = employeeRepository.findByEmployeeCode("EMP002").orElseThrow();

    createAttendance(emp1, LocalDate.of(2026, 3, 1), 9, 18);
    createAttendance(emp1, LocalDate.of(2026, 3, 2), 9, 18);

    createAttendance(emp2, LocalDate.of(2026, 3, 1), 9, 17);
    createAttendance(emp2, LocalDate.of(2026, 3, 2), 9, 17);
  }

  private void createAttendance(
      EmployeeModel employee, LocalDate date, int checkInHour, int checkOutHour) {

    if (attendanceRepository.existsByEmployee_UuidAndWorkDate(employee.getUuid(), date)) {
      return;
    }

    LocalDateTime checkIn = date.atTime(checkInHour, 0);
    LocalDateTime checkOut = date.atTime(checkOutHour, 0);

    BigDecimal totalHours = BigDecimal.valueOf(checkOutHour - checkInHour);

    attendanceRepository.save(
        AttendanceModel.builder()
            .employee(employee)
            .workDate(date)
            .checkIn(checkIn)
            .checkOut(checkOut)
            .totalHours(totalHours)
            .build());
  }
}
