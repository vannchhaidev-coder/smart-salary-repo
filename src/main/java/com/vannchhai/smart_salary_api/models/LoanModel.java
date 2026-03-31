package com.vannchhai.smart_salary_api.models;

import com.vannchhai.smart_salary_api.enums.LoanStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "loans")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LoanModel extends BaseIdModel {

  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "employee_id", nullable = false)
  private EmployeeModel employee;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private String reason;

  @Column(name = "interest_rate", nullable = false)
  private BigDecimal interestRate;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LoanStatus status;

  @Column(name = "paid_amount", precision = 15, scale = 2)
  private BigDecimal paidAmount = BigDecimal.ZERO;

  @Column(name = "remaining_balance", precision = 15, scale = 2)
  private BigDecimal remainingBalance;

  @Column(name = "risk_score")
  private Integer riskScore;

  @Column(name = "risk_level")
  private String riskLevel;

  @Column(name = "approved_by")
  private String approvedBy;

  @PrePersist
  public void prePersist() {
    if (uuid == null) uuid = UUID.randomUUID();
  }

  public boolean isLate() {
    return this.status != LoanStatus.COMPLETED && LocalDate.now().isAfter(this.endDate);
  }

  public void applyPayment(BigDecimal payment) {
    if (payment == null || payment.compareTo(BigDecimal.ZERO) <= 0) return;

    if (paidAmount == null) paidAmount = BigDecimal.ZERO;
    paidAmount = paidAmount.add(payment);

    BigDecimal remaining = amount.subtract(paidAmount);
    remainingBalance = remaining.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : remaining;

    if (remainingBalance.compareTo(BigDecimal.ZERO) <= 0) {
      status = LoanStatus.COMPLETED;
    }
  }

  public BigDecimal getMonthlyDeduction() {
    if (this.amount == null || this.interestRate == null) return BigDecimal.ZERO;

    int months = getRepaymentMonths();
    if (months <= 0) return BigDecimal.ZERO;

    BigDecimal interest =
        this.amount
            .multiply(this.interestRate)
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

    BigDecimal total = this.amount.add(interest);

    return total.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
  }

  public int getRepaymentMonths() {
    if (this.startDate == null || this.endDate == null) return 0;

    return (int) ChronoUnit.MONTHS.between(this.startDate, this.endDate);
  }

  public BigDecimal getMonthlyDeductionFromRemaining() {
    if (this.remainingBalance == null) return BigDecimal.ZERO;

    int remainingMonths = getRemainingMonths();
    if (remainingMonths <= 0) return BigDecimal.ZERO;

    return this.remainingBalance.divide(
        BigDecimal.valueOf(remainingMonths), 2, RoundingMode.HALF_UP);
  }

  public int getRemainingMonths() {
    if (this.endDate == null) return 0;

    LocalDate now = LocalDate.now();
    if (now.isAfter(endDate)) return 0;

    return (int) ChronoUnit.MONTHS.between(now, endDate);
  }
}
