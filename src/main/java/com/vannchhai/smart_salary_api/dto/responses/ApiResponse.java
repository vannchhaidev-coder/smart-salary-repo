package com.vannchhai.smart_salary_api.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

  private boolean success;
  private String message;
  private T data;
}
