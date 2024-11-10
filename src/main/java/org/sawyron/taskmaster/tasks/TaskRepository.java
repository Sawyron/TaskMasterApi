package org.sawyron.taskmaster.tasks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findByUserIdOrderByCreatedAtDesc(UUID id, Pageable pageable);

    Optional<Task> findByIdAndUserId(UUID id, UUID userId);
}
