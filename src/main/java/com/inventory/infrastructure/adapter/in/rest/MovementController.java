package com.inventory.infrastructure.adapter.in.rest;

import com.inventory.application.service.MovementService;
import com.inventory.infrastructure.adapter.in.rest.dto.MovementDtos.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movements")
public class MovementController {
  private final MovementService movements;

  public MovementController(MovementService movements) {
    this.movements = movements;
  }

  @GetMapping
  public List<MovementResponse> list() {
    return movements.list();
  }

  @GetMapping("/{id}")
  public MovementResponse get(@PathVariable Long id) {
    return movements.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MovementResponse register(@Valid @RequestBody MovementRequest request) {
    return movements.register(request);
  }
}
