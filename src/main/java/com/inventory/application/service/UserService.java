package com.inventory.application.service;

import com.inventory.domain.model.Status;
import com.inventory.infrastructure.adapter.in.rest.dto.OtherDtos.StatusRequest;
import com.inventory.infrastructure.adapter.in.rest.dto.UserResponse;
import com.inventory.infrastructure.adapter.out.persistence.*;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  public record UserRequest(String username, String password, String name, String email, String role, String status) {}

  private final UserRepository users;
  private final RoleRepository roles;
  private final PasswordEncoder encoder;
  private final CurrentUserService currentUser;
  private final AuditService audit;

  public UserService(UserRepository users, RoleRepository roles, PasswordEncoder encoder, CurrentUserService currentUser, AuditService audit) {
    this.users = users;
    this.roles = roles;
    this.encoder = encoder;
    this.currentUser = currentUser;
    this.audit = audit;
  }

  public List<UserResponse> list() {
    return users.findAll().stream().map(Mapper::user).toList();
  }

  public UserResponse get(Long id) {
    return Mapper.user(find(id));
  }

  @Transactional
  public UserResponse create(UserRequest request) {
    if (users.existsByUsername(request.username())) throw ApiException.badRequest("Ya existe un usuario con ese username.");
    if (users.existsByEmail(request.email())) throw ApiException.badRequest("Ya existe un usuario con ese email.");
    UserEntity user = new UserEntity();
    apply(user, request);
    user.setPasswordHash(encoder.encode(request.password() == null || request.password().isBlank() ? "demo123" : request.password()));
    UserEntity saved = users.save(user);
    audit.record("Crear", "Usuario", saved.getEmail(), currentUser.current());
    return Mapper.user(saved);
  }

  @Transactional
  public UserResponse update(Long id, UserRequest request) {
    UserEntity user = find(id);
    apply(user, request);
    if (request.password() != null && !request.password().isBlank()) user.setPasswordHash(encoder.encode(request.password()));
    audit.record("Editar", "Usuario", user.getEmail(), currentUser.current());
    return Mapper.user(user);
  }

  @Transactional
  public UserResponse status(Long id, StatusRequest request) {
    UserEntity user = find(id);
    user.setStatus(Status.fromLabel(request.status()));
    audit.record("Cambiar estado", "Usuario", user.getEmail(), currentUser.current());
    return Mapper.user(user);
  }

  private UserEntity find(Long id) {
    return users.findById(id).orElseThrow(() -> ApiException.notFound("Usuario no encontrado."));
  }

  private void apply(UserEntity user, UserRequest request) {
    user.setUsername(request.username());
    user.setName(request.name());
    user.setEmail(request.email());
    user.setRole(roles.findByName(request.role()).orElseThrow(() -> ApiException.badRequest("Rol no valido.")));
    user.setStatus(Status.fromLabel(request.status()));
  }
}
