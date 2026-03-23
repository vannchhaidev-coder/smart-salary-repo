package com.vannchhai.smart_salary_api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

  private final String message;

  public BusinessException(String message) {
    super(message);
    this.message = message;
  }
}
