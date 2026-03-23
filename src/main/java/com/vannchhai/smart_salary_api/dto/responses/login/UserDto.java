package com.vannchhai.smart_salary_api.dto.responses.login;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

  private UUID uuid;
  private String email;
  private String name;
  private String role;
  private String department;
  private String employeeId;
  private String position;
  private String badge;
}
