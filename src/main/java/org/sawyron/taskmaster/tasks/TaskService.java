package org.sawyron.taskmaster.tasks;

import org.sawyron.taskmaster.tasks.dtos.TaskCreateRequest;
import org.sawyron.taskmaster.tasks.dtos.TaskResponse;
import org.sawyron.taskmaster.tasks.dtos.TaskUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    void createTask(TaskCreateRequest request, UUID userId);

    List<TaskResponse> findUserTasks(UUID userId, Pageable pageable);

    void updateTask(UUID id, UUID userId, TaskUpdateRequest request);

    void removeUserTask(UUID taskId, UUID userId);
}
