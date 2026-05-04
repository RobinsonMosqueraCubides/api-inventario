package com.inventory.infrastructure.config;

import com.inventory.domain.model.Status;
import com.inventory.infrastructure.adapter.out.persistence.UserEntity;
import com.inventory.infrastructure.adapter.out.persistence.UserRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class InventoryUserDetailsService implements UserDetailsService {
  private final UserRepository users;

  public InventoryUserDetailsService(UserRepository users) {
    this.users = users;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = users.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado."));
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPasswordHash(),
        user.getStatus() == Status.ACTIVO,
        true,
        true,
        true,
        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase()))
    );
  }
}
