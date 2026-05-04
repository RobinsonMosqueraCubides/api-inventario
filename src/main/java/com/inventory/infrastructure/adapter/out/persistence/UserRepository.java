package com.inventory.infrastructure.adapter.out.persistence;

import com.inventory.domain.model.Status;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  long countByRoleNameAndStatus(String roleName, Status status);
}
