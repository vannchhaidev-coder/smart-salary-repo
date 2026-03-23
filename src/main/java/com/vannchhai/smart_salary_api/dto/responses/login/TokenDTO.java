package com.vannchhai.smart_salary_api.dto.responses.login;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
  private String accessToken;
  private String refreshToken;
}
