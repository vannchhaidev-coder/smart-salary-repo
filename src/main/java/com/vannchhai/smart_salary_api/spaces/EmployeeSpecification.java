package com.vannchhai.smart_salary_api.spaces;

import com.vannchhai.smart_salary_api.models.EmployeeModel;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

  public static Specification<EmployeeModel> hasDepartment(String department) {

    return (root, query, cb) -> {
      if (department == null || department.isEmpty()) {
        return cb.conjunction();
      }

      Join<Object, Object> join = root.join("departments");
      return cb.equal(join.get("name"), department);
    };
  }

  public static Specification<EmployeeModel> hasPosition(String position) {
    return (root, query, cb) -> {
      if (position == null || position.isEmpty()) {
        return cb.conjunction();
      }

      Join<Object, Object> join = root.join("positions");
      return cb.equal(join.get("name"), position);
    };
  }

  public static Specification<EmployeeModel> hasBadge(String badge) {
    return (root, query, cb) -> {
      if (badge == null || badge.isEmpty()) {
        return cb.conjunction();
      }

      return cb.equal(root.get("badge"), badge);
    };
  }
}
