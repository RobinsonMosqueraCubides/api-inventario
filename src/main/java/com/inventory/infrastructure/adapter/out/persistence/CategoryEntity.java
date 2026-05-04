package com.inventory.infrastructure.adapter.out.persistence;

import com.inventory.domain.model.CategoryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class CategoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CategoryStatus status = CategoryStatus.ACTIVA;
}
