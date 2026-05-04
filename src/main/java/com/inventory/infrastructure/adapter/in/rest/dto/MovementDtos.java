package com.inventory.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class MovementDtos {
  public record MovementRequest(@NotNull Long productId, @NotBlank String type, @Min(1) Integer quantity, Integer adjustment, String note) {}
  public record MovementResponse(Long id, Long productId, String productName, String type, Integer quantity, String user, Instant date, String note) {}
}
