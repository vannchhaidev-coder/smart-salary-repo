package com.vannchhai.smart_salary_api.services.Impl;

import com.vannchhai.smart_salary_api.dto.request.LoginRequest;
import com.vannchhai.smart_salary_api.dto.responses.login.LoginData;
import com.vannchhai.smart_salary_api.dto.responses.login.LoginResponse;
import com.vannchhai.smart_salary_api.dto.responses.login.TokenDTO;
import com.vannchhai.smart_salary_api.dto.responses.login.UserDto;
import com.vannchhai.smart_salary_api.exceptions.ResourceNotFoundException;
import com.vannchhai.smart_salary_api.mapper.UserMapper;
import com.vannchhai.smart_salary_api.models.EmployeeModel;
import com.vannchhai.smart_salary_api.models.UserModel;
import com.vannchhai.smart_salary_api.repositories.EmployeeRepository;
import com.vannchhai.smart_salary_api.repositories.UserRepository;
import com.vannchhai.smart_salary_api.security.CustomUserDetailsService;
import com.vannchhai.smart_salary_api.security.JwtService;
import com.vannchhai.smart_salary_api.services.AuthService;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService customUserDetailsService;
  private final EmployeeRepository employeeRepository;
  private final JwtService jwtService;

  @Override
  public LoginResponse login(LoginRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    UserModel user = (UserModel) customUserDetailsService.loadUserByUsername(request.getEmail());
    EmployeeModel employee =
        employeeRepository
            .findByUser(user)
            .orElseThrow(
                () -> new ResourceNotFoundException("Employee", "employee", user.getEmail()));

    String accessToken = jwtService.generateToken(new HashMap<>(), user);
    String refreshToken = jwtService.generateRefreshToken(user);

    UserDto userDto = userMapper.toDto(user, employee);

    return LoginResponse.builder()
        .success(true)
        .message("Login successful")
        .data(
            LoginData.builder()
                .user(userDto)
                .tokens(
                    TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build())
                .build())
        .build();
  }

  @Override
  public LoginResponse refreshToken(String refreshToken) {
    final String userEmail = jwtService.extractUsername(refreshToken);

    if (userEmail == null) {
      throw new RuntimeException("Invalid refresh token: Subject missing");
    }
    UserModel user =
        userRepository
            .findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!jwtService.isRefreshTokenValid(refreshToken)) {
      throw new RuntimeException("Refresh token has expired or is invalid");
    }

    String accessToken = jwtService.generateToken(new HashMap<>(), user);
    String newRefreshToken = jwtService.generateRefreshToken(user);

    UserDto loginUser =
        UserDto.builder()
            .uuid(user.getUuid())
            .email(user.getEmail())
            .name(user.getName())
            .role(user.getRole().getRoleName())
            .build();

    return LoginResponse.builder()
        .success(true)
        .message("Token refreshed successfully")
        .data(
            LoginData.builder()
                .user(loginUser)
                .tokens(
                    TokenDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshToken)
                        .build())
                .build())
        .build();
  }
}
