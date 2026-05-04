package com.inventory.infrastructure.adapter.out.persistence;

import com.inventory.domain.model.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
  boolean existsBySku(String sku);
  boolean existsBySkuAndIdNot(String sku, Long id);
  boolean existsByCategoryIdAndStatus(Long categoryId, Status status);
  @Query("select p from ProductEntity p where p.stock <= p.minStock")
  List<ProductEntity> findLowStock();
}
