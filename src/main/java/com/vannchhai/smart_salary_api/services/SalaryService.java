package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.dto.request.CreateSalaryRequest;
import com.vannchhai.smart_salary_api.dto.request.UpdateSalaryRequest;
import com.vannchhai.smart_salary_api.dto.responses.ApiResponse;
import com.vannchhai.smart_salary_api.dto.responses.PaginationDto;
import com.vannchhai.smart_salary_api.dto.responses.PaginationResponse;
import com.vannchhai.smart_salary_api.dto.responses.ProcessAllSalaryResponse;
import com.vannchhai.smart_salary_api.dto.responses.salaries.SalariesResponse;
import com.vannchhai.smart_salary_api.dto.responses.salaries.SalaryResponse;
import java.util.UUID;

public interface SalaryService {
  PaginationResponse<SalariesResponse> getAllSalaries(PaginationDto pagination);

  SalaryResponse getSalaryById(UUID salaryId);

  SalaryResponse createSalary(CreateSalaryRequest request);

  SalaryResponse processSalary(UpdateSalaryRequest request);

  ApiResponse<ProcessAllSalaryResponse> processAllPendingSalaries();

  void deleteSalary(UUID uuid);
}
