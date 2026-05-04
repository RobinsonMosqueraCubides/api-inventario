package com.inventory.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "audit_logs")
public class AuditLogEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(nullable = false)
  private String action;

  @Column(nullable = false)
  private String entity;

  @Column(nullable = false)
  private String detail;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;
}
