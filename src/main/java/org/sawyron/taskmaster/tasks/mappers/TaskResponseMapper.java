package org.sawyron.taskmaster.tasks.mappers;

import org.sawyron.taskmaster.tasks.Task;
import org.sawyron.taskmaster.tasks.dtos.TaskResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TaskResponseMapper implements Function<Task, TaskResponse> {
    @Override
    public TaskResponse apply(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt()
        );
    }
}
