package com.inventory.infrastructure.adapter.in.rest;

import com.inventory.application.service.*;
import com.inventory.infrastructure.adapter.in.rest.dto.AuthDtos.*;
import com.inventory.infrastructure.adapter.in.rest.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService auth;
  private final CurrentUserService currentUser;

  public AuthController(AuthService auth, CurrentUserService currentUser) {
    this.auth = auth;
    this.currentUser = currentUser;
  }

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest request) {
    return auth.login(request);
  }

  @GetMapping("/me")
  public UserResponse me() {
    return Mapper.user(currentUser.current());
  }
}
