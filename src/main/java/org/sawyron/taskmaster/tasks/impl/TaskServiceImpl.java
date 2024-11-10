package org.sawyron.taskmaster.tasks.impl;

import org.sawyron.taskmaster.tasks.Task;
import org.sawyron.taskmaster.tasks.TaskRepository;
import org.sawyron.taskmaster.tasks.TaskService;
import org.sawyron.taskmaster.tasks.dtos.TaskCreateRequest;
import org.sawyron.taskmaster.tasks.dtos.TaskResponse;
import org.sawyron.taskmaster.tasks.dtos.TaskUpdateRequest;
import org.sawyron.taskmaster.users.User;
import org.sawyron.taskmaster.users.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final Function<TaskCreateRequest, Task> taskCreateMapper;
    private final Function<Task, TaskResponse> taskResponseMapper;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            UserRepository userRepository,
            Function<TaskCreateRequest, Task> taskCreateMapper,
            Function<Task, TaskResponse> taskResponseMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskCreateMapper = taskCreateMapper;
        this.taskResponseMapper = taskResponseMapper;
    }

    @Override
    @Transactional
    public void createTask(TaskCreateRequest request, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("user not found (id: %s)".formatted(userId)));
        Task task = taskCreateMapper.apply(request);
        task.setUser(user);
        taskRepository.save(task);
    }

    @Override
    public List<TaskResponse> findUserTasks(UUID userId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return tasks.stream()
                .map(taskResponseMapper)
                .toList();
    }

    @Override
    public void updateTask(UUID id, UUID userId, TaskUpdateRequest request) {
        Optional<Task> foundTask = taskRepository.findByIdAndUserId(id, userId);
        if (foundTask.isPresent()) {
            Task task = foundTask.get();
            task.setTitle(request.title());
            task.setDescription(request.description());
            taskRepository.save(task);
        }
    }

    @Override
    public void removeUserTask(UUID taskId, UUID userId) {
        taskRepository.findByIdAndUserId(taskId, userId)
                .ifPresent(taskRepository::delete);
    }
}
