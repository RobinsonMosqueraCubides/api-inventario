package com.inventory.application.service;

import com.inventory.infrastructure.adapter.out.persistence.*;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
  private final AuditLogRepository audits;

  public AuditService(AuditLogRepository audits) {
    this.audits = audits;
  }

  public void record(String action, String entity, String detail, UserEntity user) {
    AuditLogEntity audit = new AuditLogEntity();
    audit.setAction(action);
    audit.setEntity(entity);
    audit.setDetail(detail);
    audit.setUser(user);
    audits.save(audit);
  }
}
