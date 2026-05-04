package com.inventory.application.service;

import com.inventory.domain.model.CategoryStatus;
import com.inventory.domain.model.Status;
import com.inventory.infrastructure.adapter.in.rest.dto.CategoryDtos.CategoryRequest;
import com.inventory.infrastructure.adapter.in.rest.dto.CategoryDtos.CategoryResponse;
import com.inventory.infrastructure.adapter.out.persistence.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
  private final CategoryRepository categories;
  private final ProductRepository products;
  private final CurrentUserService currentUser;
  private final AuditService audit;

  public CategoryService(CategoryRepository categories, ProductRepository products, CurrentUserService currentUser, AuditService audit) {
    this.categories = categories;
    this.products = products;
    this.currentUser = currentUser;
    this.audit = audit;
  }

  public List<CategoryResponse> list() {
    return categories.findAll().stream().map(Mapper::category).toList();
  }

  public CategoryResponse get(Long id) {
    return Mapper.category(find(id));
  }

  @Transactional
  public CategoryResponse create(CategoryRequest request) {
    if (categories.existsByName(request.name())) throw ApiException.badRequest("Ya existe una categoria con ese nombre.");
    CategoryEntity category = new CategoryEntity();
    apply(category, request);
    CategoryEntity saved = categories.save(category);
    audit.record("Crear", "Categoria", saved.getName(), currentUser.current());
    return Mapper.category(saved);
  }

  @Transactional
  public CategoryResponse update(Long id, CategoryRequest request) {
    CategoryEntity category = find(id);
    apply(category, request);
    audit.record("Editar", "Categoria", category.getName(), currentUser.current());
    return Mapper.category(category);
  }

  @Transactional
  public void delete(Long id) {
    CategoryEntity category = find(id);
    if (products.existsByCategoryIdAndStatus(id, Status.ACTIVO)) throw ApiException.badRequest("No se puede eliminar una categoria con productos activos.");
    category.setStatus(CategoryStatus.INACTIVA);
    audit.record("Eliminar", "Categoria", category.getName(), currentUser.current());
  }

  private CategoryEntity find(Long id) {
    return categories.findById(id).orElseThrow(() -> ApiException.notFound("Categoria no encontrada."));
  }

  private void apply(CategoryEntity category, CategoryRequest request) {
    category.setName(request.name());
    category.setDescription(request.description());
    category.setStatus(CategoryStatus.fromLabel(request.status()));
  }
}
