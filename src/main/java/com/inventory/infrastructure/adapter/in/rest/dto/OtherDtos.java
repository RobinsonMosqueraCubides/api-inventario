package com.inventory.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class OtherDtos {
  public record NotificationResponse(Long id, String title, String message, String level, boolean read, Instant date) {}
  public record AuditResponse(Long id, String action, String entity, String detail, String user, String role, Instant date) {}
  public record InventoryReportRow(String category, Long products, Integer stock, BigDecimal value) {}
  public record MovementReportRow(String type, Long count) {}
  public record StatusRequest(String status) {}
  public record CompanySettingsRequest(String companyName, String nit, String email, String phone, String address) {}
  public record CompanySettingsResponse(Long id, String companyName, String nit, String email, String phone, String address) {}
}
