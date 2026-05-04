package com.inventory.infrastructure.adapter.in.rest;

import com.inventory.application.service.ProductService;
import com.inventory.infrastructure.adapter.in.rest.dto.ProductDtos.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService products;

  public ProductController(ProductService products) {
    this.products = products;
  }

  @GetMapping
  public List<ProductResponse> list() {
    return products.list();
  }

  @GetMapping("/low-stock")
  public List<ProductResponse> lowStock() {
    return products.lowStock();
  }

  @GetMapping("/{id}")
  public ProductResponse get(@PathVariable Long id) {
    return products.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResponse create(@Valid @RequestBody ProductRequest request) {
    return products.create(request);
  }

  @PutMapping("/{id}")
  public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
    return products.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    products.delete(id);
  }
}
