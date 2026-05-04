package com.inventory.application.service;

import com.inventory.infrastructure.adapter.in.rest.dto.*;
import com.inventory.infrastructure.adapter.in.rest.dto.CategoryDtos.CategoryResponse;
import com.inventory.infrastructure.adapter.in.rest.dto.MovementDtos.MovementResponse;
import com.inventory.infrastructure.adapter.in.rest.dto.OtherDtos.AuditResponse;
import com.inventory.infrastructure.adapter.in.rest.dto.OtherDtos.CompanySettingsResponse;
import com.inventory.infrastructure.adapter.in.rest.dto.OtherDtos.NotificationResponse;
import com.inventory.infrastructure.adapter.in.rest.dto.ProductDtos.ProductResponse;
import com.inventory.infrastructure.adapter.out.persistence.*;

public final class Mapper {
  private Mapper() {}

  public static UserResponse user(UserEntity user) {
    return new UserResponse(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole().getName(), user.getStatus().label());
  }

  public static CategoryResponse category(CategoryEntity category) {
    return new CategoryResponse(category.getId(), category.getName(), category.getDescription(), category.getStatus().label());
  }

  public static ProductResponse product(ProductEntity product) {
    return new ProductResponse(product.getId(), product.getName(), product.getSku(), product.getCategory().getName(), product.getStock(), product.getMinStock(), product.getPrice(), product.getStatus().label());
  }

  public static MovementResponse movement(MovementEntity movement) {
    return new MovementResponse(
        movement.getId(),
        movement.getProduct().getId(),
        movement.getProduct().getName(),
        movement.getType().label(),
        movement.getQuantity(),
        movement.getUser().getName(),
        movement.getCreatedAt(),
        movement.getNote());
  }

  public static NotificationResponse notification(NotificationEntity notification) {
    return new NotificationResponse(notification.getId(), notification.getTitle(), notification.getMessage(), notification.getLevel(), notification.isRead(), notification.getCreatedAt());
  }

  public static AuditResponse audit(AuditLogEntity audit) {
    UserEntity user = audit.getUser();
    return new AuditResponse(audit.getId(), audit.getAction(), audit.getEntity(), audit.getDetail(), user == null ? "Sistema" : user.getName(), user == null ? "sistema" : user.getRole().getName(), audit.getCreatedAt());
  }

  public static CompanySettingsResponse settings(CompanySettingsEntity settings) {
    return new CompanySettingsResponse(settings.getId(), settings.getCompanyName(), settings.getNit(), settings.getEmail(), settings.getPhone(), settings.getAddress());
  }
}
