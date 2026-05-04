package com.inventory.application.service;

import com.inventory.infrastructure.adapter.out.persistence.UserEntity;
import com.inventory.infrastructure.adapter.out.persistence.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
  private final UserRepository users;

  public CurrentUserService(UserRepository users) {
    this.users = users;
  }

  public UserEntity current() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return users.findByUsername(username).orElseThrow(() -> ApiException.notFound("Usuario autenticado no encontrado."));
  }
}
