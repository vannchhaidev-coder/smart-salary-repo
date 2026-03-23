package com.vannchhai.smart_salary_api.models;

import com.vannchhai.smart_salary_api.enums.Badge;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeModel extends BaseIdModel {
  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserModel user;

  @NotBlank(message = "Employee code is required")
  @Column(unique = true)
  private String employeeCode;

  @ManyToOne
  @JoinColumn(name = "department_id")
  private DepartmentModel department;

  @ManyToOne
  @JoinColumn(name = "position_id")
  private PositionModel position;

  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
  private WalletModel wallet;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Badge badge = Badge.GOOD;

  @PrePersist
  public void generateUuid() {
    if (uuid == null) {
      uuid = UUID.randomUUID();
    }
  }
}
