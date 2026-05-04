package com.inventory.application.service;

import com.inventory.domain.model.CategoryStatus;
import com.inventory.domain.model.Status;
import com.inventory.infrastructure.adapter.out.persistence.*;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SeedService implements CommandLineRunner {
  private final RoleRepository roles;
  private final UserRepository users;
  private final CategoryRepository categories;
  private final ProductRepository products;
  private final CompanySettingsRepository settings;
  private final PasswordEncoder encoder;

  public SeedService(RoleRepository roles, UserRepository users, CategoryRepository categories, ProductRepository products, CompanySettingsRepository settings, PasswordEncoder encoder) {
    this.roles = roles;
    this.users = users;
    this.categories = categories;
    this.products = products;
    this.settings = settings;
    this.encoder = encoder;
  }

  @Override
  @Transactional
  public void run(String... args) {
    RoleEntity admin = role("admin", "Administrador");
    RoleEntity supervisor = role("supervisor", "Supervisor");
    RoleEntity empleado = role("empleado", "Empleado");
    user("admin", "Administrador", "admin@demo.com", admin);
    user("supervisor", "Sofia Rojas", "supervisor@demo.com", supervisor);
    user("empleado", "Carlos Pena", "empleado@demo.com", empleado);

    Map<String, CategoryEntity> cats = Map.of(
        "Electronica", category("Electronica", "Equipos y accesorios tecnologicos"),
        "Oficina", category("Oficina", "Papeleria, consumibles y mobiliario"),
        "Almacen", category("Almacen", "Material operativo y repuestos")
    );
    product("Laptop Lenovo ThinkPad", "ELE-LAP-001", cats.get("Electronica"), 12, 5, "3400000");
    product("Mouse inalambrico", "ELE-MOU-044", cats.get("Electronica"), 4, 10, "85000");
    product("Caja resma carta", "OFI-PAP-018", cats.get("Oficina"), 28, 12, "145000");
    product("Cable HDMI 2m", "ALM-CAB-023", cats.get("Almacen"), 3, 8, "32000");

    if (settings.count() == 0) {
      CompanySettingsEntity company = new CompanySettingsEntity();
      company.setCompanyName("Inventario Pro");
      settings.save(company);
    }
  }

  private RoleEntity role(String name, String label) {
    return roles.findByName(name).orElseGet(() -> {
      RoleEntity role = new RoleEntity();
      role.setName(name);
      role.setLabel(label);
      return roles.save(role);
    });
  }

  private void user(String username, String name, String email, RoleEntity role) {
    if (users.existsByUsername(username)) return;
    UserEntity user = new UserEntity();
    user.setUsername(username);
    user.setPasswordHash(encoder.encode("demo123"));
    user.setName(name);
    user.setEmail(email);
    user.setRole(role);
    user.setStatus(Status.ACTIVO);
    users.save(user);
  }

  private CategoryEntity category(String name, String description) {
    return categories.findByName(name).orElseGet(() -> {
      CategoryEntity category = new CategoryEntity();
      category.setName(name);
      category.setDescription(description);
      category.setStatus(CategoryStatus.ACTIVA);
      return categories.save(category);
    });
  }

  private void product(String name, String sku, CategoryEntity category, int stock, int minStock, String price) {
    if (products.existsBySku(sku)) return;
    ProductEntity product = new ProductEntity();
    product.setName(name);
    product.setSku(sku);
    product.setCategory(category);
    product.setStock(stock);
    product.setMinStock(minStock);
    product.setPrice(new BigDecimal(price));
    product.setStatus(Status.ACTIVO);
    products.save(product);
  }
}
