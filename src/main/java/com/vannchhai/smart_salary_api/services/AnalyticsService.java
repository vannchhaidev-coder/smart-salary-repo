package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.dto.analytics.LoanAnalyticsFilter;
import com.vannchhai.smart_salary_api.dto.analytics.LoanAnalyticsResponse;
import com.vannchhai.smart_salary_api.dto.responses.AnalyticsSummaryResponse;

public interface AnalyticsService {
  AnalyticsSummaryResponse getSummary();

  LoanAnalyticsResponse getAnalytics(LoanAnalyticsFilter filter);
}
