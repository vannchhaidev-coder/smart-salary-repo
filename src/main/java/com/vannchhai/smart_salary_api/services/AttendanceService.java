package com.vannchhai.smart_salary_api.services;

import java.time.LocalDate;
import java.util.UUID;

public interface AttendanceService {

  int countWorkingDaysThisMonth();

  int countPresentDaysThisMonth(UUID employeeId);

  int countPresentDaysBetween(UUID employeeId, LocalDate start, LocalDate end);

  int countWorkingDaysBetween(LocalDate start, LocalDate end);
}
