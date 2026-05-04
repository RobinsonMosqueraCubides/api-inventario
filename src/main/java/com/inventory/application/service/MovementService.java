package com.inventory.application.service;

import com.inventory.domain.model.MovementType;
import com.inventory.domain.model.Status;
import com.inventory.infrastructure.adapter.in.rest.dto.MovementDtos.MovementRequest;
import com.inventory.infrastructure.adapter.in.rest.dto.MovementDtos.MovementResponse;
import com.inventory.infrastructure.adapter.out.persistence.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovementService {
  private final MovementRepository movements;
  private final ProductRepository products;
  private final NotificationRepository notifications;
  private final CurrentUserService currentUser;
  private final AuditService audit;

  public MovementService(MovementRepository movements, ProductRepository products, NotificationRepository notifications, CurrentUserService currentUser, AuditService audit) {
    this.movements = movements;
    this.products = products;
    this.notifications = notifications;
    this.currentUser = currentUser;
    this.audit = audit;
  }

  public List<MovementResponse> list() {
    return movements.findAll().stream().map(Mapper::movement).toList();
  }

  public MovementResponse get(Long id) {
    return Mapper.movement(movements.findById(id).orElseThrow(() -> ApiException.notFound("Movimiento no encontrado.")));
  }

  @Transactional
  public MovementResponse register(MovementRequest request) {
    UserEntity user = currentUser.current();
    ProductEntity product = products.findById(request.productId()).orElseThrow(() -> ApiException.notFound("Producto no encontrado."));
    if (product.getStatus() != Status.ACTIVO) throw ApiException.badRequest("No se pueden registrar movimientos para productos inactivos.");

    MovementType type = MovementType.fromLabel(request.type());
    if ("empleado".equals(user.getRole().getName()) && type != MovementType.SALIDA) {
      throw ApiException.badRequest("El rol empleado solo puede registrar salidas.");
    }

    int quantity = type == MovementType.AJUSTE ? safe(request.adjustment()) : safe(request.quantity());
    int delta = switch (type) {
      case ENTRADA -> quantity;
      case SALIDA -> -quantity;
      case AJUSTE -> quantity;
    };
    int nextStock = product.getStock() + delta;
    if (nextStock < 0) throw ApiException.badRequest("La salida no puede dejar el stock en negativo.");

    product.setStock(nextStock);
    MovementEntity movement = new MovementEntity();
    movement.setProduct(product);
    movement.setUser(user);
    movement.setType(type);
    movement.setQuantity(type == MovementType.AJUSTE ? delta : Math.abs(quantity));
    movement.setNote(request.note());
    MovementEntity saved = movements.save(movement);
    audit.record("Registrar movimiento", "Movimiento", type.label() + ": " + product.getName(), user);

    if (product.getStock() <= product.getMinStock()) {
      NotificationEntity notification = new NotificationEntity();
      notification.setTitle("Stock bajo");
      notification.setMessage(product.getName() + " tiene " + product.getStock() + " unidades. Minimo: " + product.getMinStock() + ".");
      notification.setLevel("Critica");
      notification.setRead(false);
      notifications.save(notification);
    }
    return Mapper.movement(saved);
  }

  private int safe(Integer value) {
    if (value == null || value == 0) throw ApiException.badRequest("La cantidad debe ser diferente de cero.");
    return value;
  }
}
