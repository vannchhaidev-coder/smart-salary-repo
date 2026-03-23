package com.vannchhai.smart_salary_api.services;

import com.vannchhai.smart_salary_api.dto.request.LoginRequest;
import com.vannchhai.smart_salary_api.dto.responses.login.LoginResponse;

public interface AuthService {
  LoginResponse login(LoginRequest request);

  LoginResponse refreshToken(String refreshToken);
}
