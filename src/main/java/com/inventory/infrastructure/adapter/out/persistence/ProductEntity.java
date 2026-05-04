package com.inventory.infrastructure.adapter.out.persistence;

import com.inventory.domain.model.Status;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String sku;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  @Column(nullable = false)
  private Integer stock = 0;

  @Column(name = "min_stock", nullable = false)
  private Integer minStock = 0;

  @Column(nullable = false)
  private BigDecimal price = BigDecimal.ZERO;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.ACTIVO;
}
