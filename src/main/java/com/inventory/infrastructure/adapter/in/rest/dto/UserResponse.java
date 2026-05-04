package com.inventory.infrastructure.adapter.in.rest.dto;

public record UserResponse(Long id, String username, String name, String email, String role, String status) {}
