package com.vannchhai.smart_salary_api.controllers.admin;

import com.vannchhai.smart_salary_api.dto.request.CreateSalaryRequest;
import com.vannchhai.smart_salary_api.dto.request.UpdateSalaryRequest;
import com.vannchhai.smart_salary_api.dto.responses.ApiResponse;
import com.vannchhai.smart_salary_api.dto.responses.PaginationDto;
import com.vannchhai.smart_salary_api.dto.responses.PaginationResponse;
import com.vannchhai.smart_salary_api.dto.responses.ProcessAllSalaryResponse;
import com.vannchhai.smart_salary_api.dto.responses.salaries.SalariesResponse;
import com.vannchhai.smart_salary_api.dto.responses.salaries.SalaryResponse;
import com.vannchhai.smart_salary_api.services.SalaryService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SalaryController.BASE_PATH)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SalaryController {
  public static final String BASE_PATH = "/api/v1/admin/salary";

  private final SalaryService salaryService;

  @GetMapping
  public ResponseEntity<PaginationResponse<SalariesResponse>> getSalaries(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

    PaginationDto pagination = new PaginationDto();
    pagination.setPage(page);
    pagination.setSize(size);

    return ResponseEntity.ok(salaryService.getAllSalaries(pagination));
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<SalaryResponse> getSalaryById(@PathVariable UUID uuid) {
    SalaryResponse response = salaryService.getSalaryById(uuid);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<SalaryResponse> createSalary(@RequestBody CreateSalaryRequest request) {

    SalaryResponse response = salaryService.createSalary(request);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{uuid}")
  public ResponseEntity<SalaryResponse> updateSalaryStatus(
      @PathVariable("uuid") UUID uuid, @RequestBody UpdateSalaryRequest request) {

    if (!uuid.equals(request.getId())) {
      throw new IllegalArgumentException("Path ID and request body ID must match");
    }

    SalaryResponse response = salaryService.processSalary(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/process-all")
  public ResponseEntity<ApiResponse<ProcessAllSalaryResponse>> processAllSalaries() {
    ApiResponse<ProcessAllSalaryResponse> response = salaryService.processAllPendingSalaries();
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{uuid}")
  public ResponseEntity<Map<String, Object>> deleteSalary(@PathVariable UUID uuid) {
    salaryService.deleteSalary(uuid);
    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("message", "Salary record deleted successfully");
    return ResponseEntity.ok(response);
  }
}
