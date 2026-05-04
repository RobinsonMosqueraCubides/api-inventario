package com.inventory.infrastructure.adapter.out.persistence;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<MovementEntity, Long> {
  List<MovementEntity> findAllByCreatedAtBetween(Instant from, Instant to);
}
