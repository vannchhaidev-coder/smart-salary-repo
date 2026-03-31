package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.dto.request.CreateSalaryRequest;
import com.vannchhai.smart_salary_api.dto.request.UpdateSalaryRequest;
import com.vannchhai.smart_salary_api.dto.responses.ApiResponse;
import com.vannchhai.smart_salary_api.dto.responses.PaginationDto;
import com.vannchhai.smart_salary_api.dto.responses.PaginationResponse;
import com.vannchhai.smart_salary_api.dto.responses.ProcessAllSalaryResponse;
import com.vannchhai.smart_salary_api.dto.responses.salaries.DeductionItem;
import com.vannchhai.smart_salary_api.dto.responses.salaries.SalariesResponse;
import com.vannchhai.smart_salary_api.dto.responses.salaries.SalaryData;
import com.vannchhai.smart_salary_api.dto.responses.salaries.SalaryResponse;
import com.vannchhai.smart_salary_api.enums.LoanStatus;
import com.vannchhai.smart_salary_api.exceptions.ResourceNotFoundException;
import com.vannchhai.smart_salary_api.mapper.SalaryMapper;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.LoanModel;
import com.vannchhai.smart_salary_api.models.PayrollModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.LoanRepository;
import com.vannchhai.smart_salary_api.repositories.PayrollRepository;
import com.vannchhai.smart_salary_api.repositories.SalaryRepository;
import com.vannchhai.smart_salary_api.services.SalaryService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {

  private final PayrollRepository payrollRepository;
  private final SalaryMapper salaryMapper;
  private final EmployeeRepository employeeRepository;
  private final SalaryRepository salaryRepository;
  private final LoanRepository loanRepository;

  @Override
  public PaginationResponse<SalariesResponse> getAllSalaries(PaginationDto pagination) {

    Pageable pageable =
        PageRequest.of(
            pagination.getPage(),
            pagination.getSize(),
            Sort.by("payYear").descending().and(Sort.by("payMonth").descending()));

    Page<PayrollModel> payrollPage = payrollRepository.findAll(pageable);

    List<SalariesResponse> data =
        payrollPage.getContent().stream().map(salaryMapper::toResponse).toList();

    pagination.setTotal(payrollPage.getTotalElements());
    pagination.setTotalPages(payrollPage.getTotalPages());

    return new PaginationResponse<>(data, pagination);
  }

  @Override
  public SalaryResponse getSalaryById(UUID payrollUuid) {

    PayrollModel payroll =
        payrollRepository
            .findByUuid(payrollUuid)
            .orElseThrow(() -> new ResourceNotFoundException("Payroll", "uuid", payrollUuid));

    List<LoanModel> loans = loanRepository.findByEmployee_Uuid(payroll.getEmployee().getUuid());

    SalaryData salaryData = salaryMapper.toSalaryData(payroll, loans);

    List<DeductionItem> deductionItems = loans.stream().map(salaryMapper::toDeductionItem).toList();
    salaryData.setDeductionBreakdown(deductionItems);

    return new SalaryResponse(true, salaryData);
  }

  @Override
  public SalaryResponse createSalary(CreateSalaryRequest request) {

    EmployeeModel employee =
        employeeRepository
            .findByEmployeeCode(request.getEmployeeId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Employee", "employeeCode", request.getEmployeeId()));

    String[] parts = request.getMonth().split("-");
    int year = Integer.parseInt(parts[0]);
    int month = Integer.parseInt(parts[1]);

    List<LoanModel> activeLoans =
        loanRepository.findByEmployeeAndStatusIn(employee, List.of(LoanStatus.REPAYING));

    BigDecimal totalDeduction;

    if (request.getDeductions() != null) {
      totalDeduction = request.getDeductions();
    } else {
      totalDeduction =
          activeLoans.stream()
              .map(LoanModel::getMonthlyDeduction)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    BigDecimal netSalary = request.getBaseSalary().subtract(totalDeduction);

    PayrollModel payroll = new PayrollModel();
    payroll.setEmployee(employee);
    payroll.setBaseSalary(request.getBaseSalary());
    payroll.setTotalDeduction(totalDeduction);
    payroll.setNetSalary(netSalary);
    payroll.setPayYear(year);
    payroll.setPayMonth(month);

    payrollRepository.save(payroll);

    for (LoanModel loan : activeLoans) {
      BigDecimal monthly = loan.getMonthlyDeduction();
      loan.applyPayment(monthly);
    }

    loanRepository.saveAll(activeLoans);

    SalaryData data = salaryMapper.toSalaryData(payroll, activeLoans);

    return new SalaryResponse(true, data);
  }

  @Override
  public SalaryResponse processSalary(UpdateSalaryRequest request) {

    PayrollModel payroll =
        payrollRepository
            .findByUuid(request.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", request.getId()));

    if (!"processed".equalsIgnoreCase(payroll.getStatus())) {
      payroll.setStatus("processed");
      payroll.setProcessedDate(LocalDateTime.now());

      List<LoanModel> activeLoans =
          loanRepository.findByEmployeeAndStatusIn(
              payroll.getEmployee(), List.of(LoanStatus.REPAYING, LoanStatus.APPROVED));

      BigDecimal totalDeduction =
          activeLoans.stream()
              .map(LoanModel::getMonthlyDeduction)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

      payroll.setTotalDeduction(totalDeduction);
      payroll.setNetSalary(payroll.getBaseSalary().subtract(totalDeduction));

      payrollRepository.save(payroll);
    }

    List<LoanModel> activeLoans =
        loanRepository.findByEmployeeAndStatusIn(
            payroll.getEmployee(), List.of(LoanStatus.REPAYING, LoanStatus.APPROVED));

    SalaryData data = salaryMapper.toSalaryData(payroll, activeLoans);
    return new SalaryResponse(true, data);
  }

  @Override
  public ApiResponse<ProcessAllSalaryResponse> processAllPendingSalaries() {
    List<PayrollModel> pendingPayrolls = payrollRepository.findByStatus("pending");

    int processedCount = 0;
    BigDecimal totalNetAmount = BigDecimal.ZERO;

    for (PayrollModel payroll : pendingPayrolls) {

      payroll.setStatus("processed");
      payroll.setProcessedDate(LocalDateTime.now());

      List<LoanModel> activeLoans =
          loanRepository.findByEmployeeAndStatusIn(
              payroll.getEmployee(), List.of(LoanStatus.REPAYING, LoanStatus.APPROVED));

      BigDecimal totalDeduction =
          activeLoans.stream()
              .map(LoanModel::getMonthlyDeduction)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

      payroll.setTotalDeduction(totalDeduction);
      payroll.setNetSalary(
          payroll
              .getBaseSalary()
              .add(
                  payroll.getTotalAllowance() != null
                      ? payroll.getTotalAllowance()
                      : BigDecimal.ZERO)
              .subtract(totalDeduction));

      payrollRepository.save(payroll);

      processedCount++;
      totalNetAmount = totalNetAmount.add(payroll.getNetSalary());
    }

    ProcessAllSalaryResponse data = new ProcessAllSalaryResponse(processedCount, totalNetAmount);
    return new ApiResponse<>(
        true, "Successfully processed " + processedCount + " salary records", data);
  }

  @Override
  public void deleteSalary(UUID uuid) {
    PayrollModel payroll =
        payrollRepository
            .findByUuid(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Payroll", "id", uuid));
    payrollRepository.delete(payroll);
  }
}
