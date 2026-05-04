package com.inventory.infrastructure.adapter.in.rest;

import com.inventory.application.service.QueryService;
import com.inventory.infrastructure.adapter.in.rest.dto.OtherDtos.*;
import com.inventory.infrastructure.adapter.in.rest.dto.ProductDtos.ProductResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class QueryController {
  private final QueryService query;

  public QueryController(QueryService query) {
    this.query = query;
  }

  @GetMapping("/api/reports/inventory")
  public List<InventoryReportRow> inventoryReport() {
    return query.inventoryReport();
  }

  @GetMapping("/api/reports/movements")
  public List<MovementReportRow> movementReport() {
    return query.movementReport();
  }

  @GetMapping("/api/reports/low-stock")
  public List<ProductResponse> lowStockReport() {
    return query.lowStockReport();
  }

  @GetMapping("/api/notifications")
  public List<NotificationResponse> notifications() {
    return query.notifications();
  }

  @PatchMapping("/api/notifications/{id}/read")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void markRead(@PathVariable Long id) {
    query.markRead(id);
  }

  @PatchMapping("/api/notifications/read-all")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void markAllRead() {
    query.markAllRead();
  }

  @GetMapping("/api/audit")
  public List<AuditResponse> audit() {
    return query.audit();
  }

  @GetMapping("/api/audit/{id}")
  public AuditResponse audit(@PathVariable Long id) {
    return query.audit(id);
  }

  @GetMapping("/api/settings/company")
  public CompanySettingsResponse company() {
    return query.company();
  }

  @PutMapping("/api/settings/company")
  public CompanySettingsResponse updateCompany(@RequestBody CompanySettingsRequest request) {
    return query.updateCompany(request);
  }
}
