package com.vannchhai.smart_salary_api.controllers;

import com.vannchhai.smart_salary_api.dto.analytics.LoanAnalyticsFilter;
import com.vannchhai.smart_salary_api.dto.analytics.LoanAnalyticsResponse;
import com.vannchhai.smart_salary_api.dto.responses.AnalyticsSummaryResponse;
import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AnalyticsController.BASE_PATH)
@RequiredArgsConstructor
public class AnalyticsController {

  public static final String BASE_PATH = "/api/v1/analytics";
  private final AnalyticsService analyticsService;

  @GetMapping("/summary")
  public ResponseEntity<AnalyticsSummaryResponse> getSummary() {
    return ResponseEntity.ok(analyticsService.getSummary());
  }

  @GetMapping("/loans")
  public ResponseEntity<LoanAnalyticsResponse> getLoanAnalytics(
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) String department,
      @RequestParam(required = false) LoanStatus status) {

    LoanAnalyticsFilter filter = new LoanAnalyticsFilter();
    filter.setYear(year);
    filter.setDepartment(department);
    filter.setStatus(status);

    return ResponseEntity.ok(analyticsService.getAnalytics(filter));
  }
}
