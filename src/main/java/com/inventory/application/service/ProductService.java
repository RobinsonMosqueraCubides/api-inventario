package com.inventory.application.service;

import com.inventory.domain.model.Status;
import com.inventory.infrastructure.adapter.in.rest.dto.ProductDtos.ProductRequest;
import com.inventory.infrastructure.adapter.in.rest.dto.ProductDtos.ProductResponse;
import com.inventory.infrastructure.adapter.out.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
  private final ProductRepository products;
  private final CategoryRepository categories;
  private final CurrentUserService currentUser;
  private final AuditService audit;

  public ProductService(ProductRepository products, CategoryRepository categories, CurrentUserService currentUser, AuditService audit) {
    this.products = products;
    this.categories = categories;
    this.currentUser = currentUser;
    this.audit = audit;
  }

  public List<ProductResponse> list() {
    return products.findAll().stream().map(Mapper::product).toList();
  }

  public ProductResponse get(Long id) {
    return Mapper.product(find(id));
  }

  public List<ProductResponse> lowStock() {
    return products.findLowStock().stream().map(Mapper::product).toList();
  }

  @Transactional
  public ProductResponse create(ProductRequest request) {
    if (products.existsBySku(request.sku())) throw ApiException.badRequest("Ya existe un producto con ese SKU.");
    ProductEntity product = new ProductEntity();
    apply(product, request);
    ProductEntity saved = products.save(product);
    audit.record("Crear", "Producto", saved.getName(), currentUser.current());
    return Mapper.product(saved);
  }

  @Transactional
  public ProductResponse update(Long id, ProductRequest request) {
    ProductEntity product = find(id);
    if (products.existsBySkuAndIdNot(request.sku(), id)) throw ApiException.badRequest("Ya existe un producto con ese SKU.");
    apply(product, request);
    audit.record("Editar", "Producto", product.getName(), currentUser.current());
    return Mapper.product(product);
  }

  @Transactional
  public void delete(Long id) {
    ProductEntity product = find(id);
    product.setStatus(Status.INACTIVO);
    audit.record("Eliminar", "Producto", product.getName(), currentUser.current());
  }

  ProductEntity find(Long id) {
    return products.findById(id).orElseThrow(() -> ApiException.notFound("Producto no encontrado."));
  }

  private void apply(ProductEntity product, ProductRequest request) {
    CategoryEntity category = resolveCategory(request);
    product.setName(request.name());
    product.setSku(request.sku());
    product.setCategory(category);
    product.setStock(request.stock() == null ? 0 : request.stock());
    product.setMinStock(request.minStock() == null ? 0 : request.minStock());
    product.setPrice(request.price() == null ? BigDecimal.ZERO : request.price());
    product.setStatus(Status.fromLabel(request.status()));
  }

  private CategoryEntity resolveCategory(ProductRequest request) {
    if (request.categoryId() != null) {
      return categories.findById(request.categoryId()).orElseThrow(() -> ApiException.notFound("Categoria no encontrada."));
    }
    if (request.category() != null && !request.category().isBlank()) {
      return categories.findByName(request.category()).orElseThrow(() -> ApiException.notFound("Categoria no encontrada."));
    }
    throw ApiException.badRequest("La categoria es obligatoria.");
  }
}
