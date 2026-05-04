package com.inventory.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class NotificationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  private String level;

  @Column(name = "is_read", nullable = false)
  private boolean read;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;
}
