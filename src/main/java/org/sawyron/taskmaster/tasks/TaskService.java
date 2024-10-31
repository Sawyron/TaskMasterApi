package org.sawyron.taskmaster.tasks;

import org.sawyron.taskmaster.tasks.dtos.TaskCreateRequest;
import org.sawyron.taskmaster.tasks.dtos.TaskResponse;
import org.sawyron.taskmaster.tasks.dtos.TaskUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    void createTask(TaskCreateRequest request, UUID userId);

    List<TaskResponse> findUserTasks(UUID userId);

    void updateTask(UUID id, UUID userId, TaskUpdateRequest request);

    void removeUserTask(UUID taskId, UUID userId);
}
