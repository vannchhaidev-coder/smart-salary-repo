package com.vannchhai.smart_salary_api.controllers.admin;

import com.vannchhai.smart_salary_api.services.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PayrollController.BASE_URL)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PayrollController {

  public static final String BASE_URL = "/api/admin/payroll";

  private final PayrollService payrollService;

  @PostMapping("/generate")
  public ResponseEntity<String> generatePayroll(@RequestParam int month, @RequestParam int year) {

    payrollService.generatePayroll(month, year);
    return ResponseEntity.ok("Payroll generated successfully");
  }
}
