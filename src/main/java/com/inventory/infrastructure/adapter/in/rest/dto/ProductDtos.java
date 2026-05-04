package com.inventory.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductDtos {
  public record ProductRequest(
      @NotBlank String name,
      @NotBlank String sku,
      Long categoryId,
      String category,
      @Min(0) Integer stock,
      @Min(0) Integer minStock,
      @DecimalMin("0.0") BigDecimal price,
      String status
  ) {}

  public record ProductResponse(Long id, String name, String sku, String category, Integer stock, Integer minStock, BigDecimal price, String status) {}
}
