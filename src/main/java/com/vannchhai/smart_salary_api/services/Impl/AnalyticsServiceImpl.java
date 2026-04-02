package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.dto.analytics.LoanAnalyticsFilter;
import com.vannchhai.smart_salary_api.dto.analytics.LoanAnalyticsResponse;
import com.vannchhai.smart_salary_api.dto.responses.AnalyticsSummaryResponse;
import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.repositories.AnalyticsCustomRepository;
import com.vannchhai.smart_salary_api.repositories.LoanRepository;
import com.vannchhai.smart_salary_api.services.AnalyticsService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

  private final LoanRepository loanRepository;
  private final AnalyticsCustomRepository repository;

  @Override
  public AnalyticsSummaryResponse getSummary() {

    long totalLoans = loanRepository.countAllLoans();
    long approvedLoans =
        loanRepository.countByStatusIn(List.of(LoanStatus.APPROVED, LoanStatus.COMPLETED));

    long completedLoans = loanRepository.countByStatus(LoanStatus.COMPLETED);

    BigDecimal totalOutstanding = loanRepository.getTotalOutstanding();

    int approvalRate = totalLoans == 0 ? 0 : (int) ((approvedLoans * 100.0) / totalLoans);

    int repaymentRate = approvedLoans == 0 ? 0 : (int) ((completedLoans * 100.0) / approvedLoans);

    return new AnalyticsSummaryResponse(totalLoans, totalOutstanding, approvalRate, repaymentRate);
  }

  @Override
  public LoanAnalyticsResponse getAnalytics(LoanAnalyticsFilter filter) {
    return repository.getAnalytics(filter);
  }
}
