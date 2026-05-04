package com.inventory.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDtos {
  public record CategoryRequest(@NotBlank String name, String description, String status) {}
  public record CategoryResponse(Long id, String name, String description, String status) {}
}
