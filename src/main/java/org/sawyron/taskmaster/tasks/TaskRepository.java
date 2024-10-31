package org.sawyron.taskmaster.tasks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByUserIdOrderByCreatedAtDesc(UUID id);

    Optional<Task> findByIdAndUserId(UUID id, UUID userId);
}
