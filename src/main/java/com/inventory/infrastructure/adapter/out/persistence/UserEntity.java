package com.inventory.infrastructure.adapter.out.persistence;

import com.inventory.domain.model.Status;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id")
  private RoleEntity role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.ACTIVO;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;
}
