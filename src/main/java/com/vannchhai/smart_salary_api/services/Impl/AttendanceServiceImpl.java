package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.repositories.AttendanceRepository;
import com.vannchhai.smart_salary_api.services.AttendanceService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

  private final AttendanceRepository attendanceRepository;

  public int countWorkingDaysThisMonth() {
    LocalDate now = LocalDate.now();

    LocalDate start = now.withDayOfMonth(1);
    LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

    int workingDays = 0;

    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
      DayOfWeek day = date.getDayOfWeek();

      if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
        workingDays++;
      }
    }

    return workingDays;
  }

  @Override
  public int countPresentDaysThisMonth(UUID employeeId) {
    LocalDate start = LocalDate.now().withDayOfMonth(1);
    LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

    return attendanceRepository.countPresentDaysBetween(employeeId, start, end);
  }

  @Override
  public int countPresentDaysBetween(UUID employeeId, LocalDate start, LocalDate end) {
    return attendanceRepository.countPresentDaysBetween(employeeId, start, end);
  }

  @Override
  public int countWorkingDaysBetween(LocalDate start, LocalDate end) {
    int workingDays = 0;
    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
      DayOfWeek day = date.getDayOfWeek();
      if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
        workingDays++;
      }
    }
    return workingDays;
  }
}
