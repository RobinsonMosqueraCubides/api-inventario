package com.inventory.infrastructure.adapter.in.rest;

import com.inventory.application.service.CategoryService;
import com.inventory.infrastructure.adapter.in.rest.dto.CategoryDtos.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
  private final CategoryService categories;

  public CategoryController(CategoryService categories) {
    this.categories = categories;
  }

  @GetMapping
  public List<CategoryResponse> list() {
    return categories.list();
  }

  @GetMapping("/{id}")
  public CategoryResponse get(@PathVariable Long id) {
    return categories.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryResponse create(@Valid @RequestBody CategoryRequest request) {
    return categories.create(request);
  }

  @PutMapping("/{id}")
  public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
    return categories.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    categories.delete(id);
  }
}
