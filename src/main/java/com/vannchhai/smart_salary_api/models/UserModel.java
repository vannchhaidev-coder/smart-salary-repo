package com.vannchhai.smart_salary_api.models;

import com.vannchhai.smart_salary_api.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "users")
public class UserModel extends BaseIdModel implements UserDetails {

  @NotBlank(message = "Name is required")
  @Size(min = 2, max = 100)
  private String name;

  @Email(message = "Email must be valid")
  @NotBlank(message = "Email is required")
  @Size(max = 150)
  @Column(unique = true, nullable = false)
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @ToString.Exclude
  @NotNull(message = "Role is required")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id", nullable = false)
  private UserRoleModel role;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserStatus status = UserStatus.ACTIVE;

  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @PrePersist
  protected void onCreate() {
    if (uuid == null) {
      uuid = UUID.randomUUID();
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
    return authorities;
  }

  @Override
  public String getPassword() {
    return passwordHash;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
