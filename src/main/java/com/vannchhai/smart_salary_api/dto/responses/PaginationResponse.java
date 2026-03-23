package com.vannchhai.smart_salary_api.dto.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationResponse<T> {
  private List<T> data;
  private PaginationDto pagination;
}
