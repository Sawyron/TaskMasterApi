package org.sawyron.taskmaster.tasks.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(UUID id, String title, String description, LocalDateTime createdAt) {
}
