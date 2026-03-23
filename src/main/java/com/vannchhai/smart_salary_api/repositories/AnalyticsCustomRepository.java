package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.dto.analytics.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnalyticsCustomRepository {

  private final EntityManager em;

  public LoanAnalyticsResponse getAnalytics(LoanAnalyticsFilter filter) {

    StringBuilder monthlySql =
        new StringBuilder(
            """
            SELECT
                TO_CHAR(l.start_date, 'Mon') as month,
                SUM(l.amount),
                COUNT(*)
            FROM loans l
            JOIN employees e ON l.employee_id = e.id
            WHERE 1=1
        """);

    if (filter.getYear() != null) {
      monthlySql.append(" AND EXTRACT(YEAR FROM l.start_date) = :year");
    }

    if (filter.getDepartment() != null) {
      monthlySql.append(" AND e.department = :department");
    }

    if (filter.getStatus() != null) {
      monthlySql.append(" AND l.status = :status");
    }

    monthlySql.append(
        """
            GROUP BY month, EXTRACT(MONTH FROM l.start_date)
            ORDER BY EXTRACT(MONTH FROM l.start_date)
        """);

    Query monthlyQuery = em.createNativeQuery(monthlySql.toString());
    setParams(monthlyQuery, filter);

    List<Object[]> monthlyResult = monthlyQuery.getResultList();

    List<MonthlyLoanDto> monthly =
        monthlyResult.stream()
            .map(
                row ->
                    new MonthlyLoanDto(
                        (String) row[0], (BigDecimal) row[1], ((Number) row[2]).longValue()))
            .toList();

    StringBuilder riskSql =
        new StringBuilder(
            """
            SELECT l.risk_level, COUNT(*)
            FROM loans l
            WHERE 1=1
        """);

    if (filter.getYear() != null) {
      riskSql.append(" AND EXTRACT(YEAR FROM l.start_date) = :year");
    }

    riskSql.append(" GROUP BY l.risk_level");

    Query riskQuery = em.createNativeQuery(riskSql.toString());

    setParams(riskQuery, filter);

    List<Object[]> riskResult = riskQuery.getResultList();

    List<RiskDistributionDto> risk =
        riskResult.stream()
            .map(row -> new RiskDistributionDto((String) row[0], ((Number) row[1]).longValue()))
            .toList();

    StringBuilder deptSql = new StringBuilder("""
        SELECT d.name, SUM(l.amount)
        FROM loans l
        JOIN employees e ON l.employee_id = e.id
        JOIN departments d ON e.department_id = d.id
        WHERE 1=1
    """);

    if (filter.getYear() != null) {
      deptSql.append(" AND EXTRACT(YEAR FROM l.start_date) = :year");
    }

    if (filter.getDepartment() != null) {
      deptSql.append(" AND d.name = :department");
    }

    deptSql.append(" GROUP BY d.name ORDER BY d.name");

    Query deptQuery = em.createNativeQuery(deptSql.toString());

    if (filter.getYear() != null) {
      deptQuery.setParameter("year", filter.getYear());
    }
    if (filter.getDepartment() != null) {
      deptQuery.setParameter("department", filter.getDepartment());
    }

    @SuppressWarnings("unchecked")
    List<Object[]> result = deptQuery.getResultList();

    List<DepartmentLoanDto> dept = result.stream()
            .map(row -> new DepartmentLoanDto(
                    (String) row[0],
                    (BigDecimal) row[1]
            ))
            .toList();
    return new LoanAnalyticsResponse(monthly, risk, dept);
  }

  private void setParams(Query query, LoanAnalyticsFilter filter) {
    if (filter.getYear() != null) {
      query.setParameter("year", filter.getYear());
    }
    if (filter.getDepartment() != null) {
      query.setParameter("department", filter.getDepartment());
    }
    if (filter.getStatus() != null) {
      query.setParameter("status", filter.getStatus().name());
    }
  }
}
