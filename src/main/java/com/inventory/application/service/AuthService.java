package com.inventory.application.service;

import com.inventory.domain.model.Status;
import com.inventory.infrastructure.adapter.in.rest.dto.AuthDtos.LoginRequest;
import com.inventory.infrastructure.adapter.in.rest.dto.AuthDtos.LoginResponse;
import com.inventory.infrastructure.adapter.out.persistence.UserEntity;
import com.inventory.infrastructure.adapter.out.persistence.UserRepository;
import com.inventory.infrastructure.config.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final AuthenticationManager authManager;
  private final UserRepository users;
  private final JwtService jwt;

  public AuthService(AuthenticationManager authManager, UserRepository users, JwtService jwt) {
    this.authManager = authManager;
    this.users = users;
    this.jwt = jwt;
  }

  public LoginResponse login(LoginRequest request) {
    try {
      authManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    } catch (AuthenticationException ex) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Usuario o contrasena invalida.");
    }
    UserEntity user = users.findByUsername(request.username()).orElseThrow();
    if (user.getStatus() != Status.ACTIVO) throw new ApiException(HttpStatus.UNAUTHORIZED, "Usuario inactivo.");
    return new LoginResponse(jwt.createToken(user), Mapper.user(user));
  }
}
