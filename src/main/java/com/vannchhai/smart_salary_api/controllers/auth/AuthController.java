package com.vannchhai.smart_salary_api.controllers.auth;

import com.vannchhai.smart_salary_api.dto.request.LoginRequest;
import com.vannchhai.smart_salary_api.dto.request.RefreshRequest;
import com.vannchhai.smart_salary_api.dto.responses.login.LoginResponse;
import com.vannchhai.smart_salary_api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthController.BASE_URL)
public class AuthController {

  public static final String BASE_URL = "/api/v1/auth";
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse response = authService.login(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshRequest request) {
    LoginResponse response = authService.refreshToken(request.getRefreshToken());
    return ResponseEntity.ok(response);
  }
}
