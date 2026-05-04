package com.inventory.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
  public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
  public record LoginResponse(String token, UserResponse user) {}
}
