package com.inventory.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
  List<NotificationEntity> findByUserIdOrUserIsNullOrderByCreatedAtDesc(Long userId);
}
