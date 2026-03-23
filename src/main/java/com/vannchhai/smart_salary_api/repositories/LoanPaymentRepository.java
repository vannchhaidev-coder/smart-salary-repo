package com.vannchhai.smart_salary_api.repositories;

import com.vannchhai.smart_salary_api.models.LoanPaymentModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanPaymentRepository extends JpaRepository<LoanPaymentModel, Long> {

  List<LoanPaymentModel> findByLoanIdOrderByPaymentDateAsc(Long loanId);
}
