package com.vannchhai.smart_salary_api.dto.responses;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PaginationDto {
  @Min(value = 0, message = "page must not be less than 0")
  private Integer page = 0;

  @Min(value = 1, message = "size must not be less than 1")
  private Integer size = 10;

  private Integer totalPages;
  private Long total;
}
