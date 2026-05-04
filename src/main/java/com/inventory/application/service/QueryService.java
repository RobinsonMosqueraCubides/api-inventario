package com.inventory.application.service;

import com.inventory.infrastructure.adapter.in.rest.dto.OtherDtos.*;
import com.inventory.infrastructure.adapter.in.rest.dto.ProductDtos.ProductResponse;
import com.inventory.infrastructure.adapter.out.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QueryService {
  private final ProductRepository products;
  private final MovementRepository movements;
  private final NotificationRepository notifications;
  private final AuditLogRepository audits;
  private final CompanySettingsRepository settings;
  private final CurrentUserService currentUser;

  public QueryService(ProductRepository products, MovementRepository movements, NotificationRepository notifications, AuditLogRepository audits, CompanySettingsRepository settings, CurrentUserService currentUser) {
    this.products = products;
    this.movements = movements;
    this.notifications = notifications;
    this.audits = audits;
    this.settings = settings;
    this.currentUser = currentUser;
  }

  public List<InventoryReportRow> inventoryReport() {
    return products.findAll().stream()
        .collect(Collectors.groupingBy(product -> product.getCategory().getName()))
        .entrySet().stream()
        .map(entry -> new InventoryReportRow(
            entry.getKey(),
            (long) entry.getValue().size(),
            entry.getValue().stream().mapToInt(ProductEntity::getStock).sum(),
            entry.getValue().stream().map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getStock()))).reduce(BigDecimal.ZERO, BigDecimal::add)))
        .toList();
  }

  public List<MovementReportRow> movementReport() {
    return movements.findAll().stream()
        .collect(Collectors.groupingBy(movement -> movement.getType().label(), Collectors.counting()))
        .entrySet().stream()
        .map(entry -> new MovementReportRow(entry.getKey(), entry.getValue()))
        .toList();
  }

  public List<ProductResponse> lowStockReport() {
    return products.findLowStock().stream().map(Mapper::product).toList();
  }

  public List<NotificationResponse> notifications() {
    return notifications.findByUserIdOrUserIsNullOrderByCreatedAtDesc(currentUser.current().getId()).stream().map(Mapper::notification).toList();
  }

  @Transactional
  public void markRead(Long id) {
    NotificationEntity notification = notifications.findById(id).orElseThrow(() -> ApiException.notFound("Notificacion no encontrada."));
    notification.setRead(true);
  }

  @Transactional
  public void markAllRead() {
    notifications().forEach(notification -> markRead(notification.id()));
  }

  public List<AuditResponse> audit() {
    return audits.findAll().stream().map(Mapper::audit).toList();
  }

  public AuditResponse audit(Long id) {
    return Mapper.audit(audits.findById(id).orElseThrow(() -> ApiException.notFound("Auditoria no encontrada.")));
  }

  public CompanySettingsResponse company() {
    return Mapper.settings(settings.findAll().stream().findFirst().orElseThrow(() -> ApiException.notFound("Configuracion no encontrada.")));
  }

  @Transactional
  public CompanySettingsResponse updateCompany(CompanySettingsRequest request) {
    CompanySettingsEntity entity = settings.findAll().stream().findFirst().orElseGet(CompanySettingsEntity::new);
    entity.setCompanyName(request.companyName());
    entity.setNit(request.nit());
    entity.setEmail(request.email());
    entity.setPhone(request.phone());
    entity.setAddress(request.address());
    return Mapper.settings(settings.save(entity));
  }
}
