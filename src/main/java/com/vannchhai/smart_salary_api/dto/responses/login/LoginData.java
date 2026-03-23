package com.vannchhai.smart_salary_api.dto.responses.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginData {
  private UserDto user;
  private TokenDTO tokens;
}
