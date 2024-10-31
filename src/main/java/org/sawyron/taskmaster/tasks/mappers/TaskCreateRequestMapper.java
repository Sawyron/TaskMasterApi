package org.sawyron.taskmaster.tasks.mappers;

import org.sawyron.taskmaster.tasks.Task;
import org.sawyron.taskmaster.tasks.dtos.TaskCreateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Function;

@Component
public class TaskCreateRequestMapper implements Function<TaskCreateRequest, Task> {
    @Override
    public Task apply(TaskCreateRequest request) {
        var task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCreatedAt(LocalDateTime.now());
        return task;
    }
}
