package com.vannchhai.smart_salary_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class UserRoleModel extends BaseIdModel {

  @NotBlank(message = "Role name is required")
  @Size(min = 3, max = 50, message = "Role name must be between 3 and 50 characters")
  @Column(name = "role_name", nullable = false, unique = true)
  private String roleName;

  @Size(max = 255, message = "Description cannot exceed 255 characters")
  private String description;

  @Builder.Default
  @JsonIgnore
  @ToString.Exclude
  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
  private List<UserModel> users = new ArrayList<>();
}
