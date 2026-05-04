package com.inventory.infrastructure.adapter.out.persistence;

import com.inventory.domain.model.MovementType;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "movements")
public class MovementEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id")
  private ProductEntity product;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MovementType type;

  @Column(nullable = false)
  private Integer quantity;

  private String note;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;
}
