package com.inventory.infrastructure.adapter.in.rest;

import com.inventory.application.service.UserService;
import com.inventory.application.service.UserService.UserRequest;
import com.inventory.infrastructure.adapter.in.rest.dto.OtherDtos.StatusRequest;
import com.inventory.infrastructure.adapter.in.rest.dto.UserResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService users;

  public UserController(UserService users) {
    this.users = users;
  }

  @GetMapping
  public List<UserResponse> list() {
    return users.list();
  }

  @GetMapping("/{id}")
  public UserResponse get(@PathVariable Long id) {
    return users.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse create(@RequestBody UserRequest request) {
    return users.create(request);
  }

  @PutMapping("/{id}")
  public UserResponse update(@PathVariable Long id, @RequestBody UserRequest request) {
    return users.update(id, request);
  }

  @PatchMapping("/{id}/status")
  public UserResponse status(@PathVariable Long id, @RequestBody StatusRequest request) {
    return users.status(id, request);
  }
}
