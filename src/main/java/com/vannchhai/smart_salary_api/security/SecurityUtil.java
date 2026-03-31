package com.vannchhai.smart_salary_api.security;

import com.vannchhai.smart_salary_api.models.UserModel;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

  public UserModel getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (UserModel) authentication.getPrincipal();
  }

  public String getEmail() {
    return getCurrentUser().getEmail();
  }

  public UUID getUserUuid() {
    return getCurrentUser().getUuid();
  }
}
