package org.sawyron.taskmaster.tasks.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sawyron.taskmaster.tasks.Task;
import org.sawyron.taskmaster.tasks.TaskRepository;
import org.sawyron.taskmaster.tasks.dtos.TaskCreateRequest;
import org.sawyron.taskmaster.tasks.dtos.TaskResponse;
import org.sawyron.taskmaster.tasks.dtos.TaskUpdateRequest;
import org.sawyron.taskmaster.tasks.mappers.TaskCreateRequestMapper;
import org.sawyron.taskmaster.tasks.mappers.TaskResponseMapper;
import org.sawyron.taskmaster.users.User;
import org.sawyron.taskmaster.users.UserRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private Function<TaskCreateRequest, Task> taskCreateRequestMapper = new TaskCreateRequestMapper();

    @Spy
    private Function<Task, TaskResponse> taskResponseMapper = new TaskResponseMapper();

    @BeforeEach
    void setup() {
        taskService = new TaskServiceImpl(
                taskRepository,
                userRepository,
                taskCreateRequestMapper,
                taskResponseMapper
        );
    }

    @Test
    void whenCreateTaskRequestIsValid_thenCreateTaskSucceeds() {
        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        User user = new User();
        user.setId(UUID.fromString("bbcc4621-d88f-4a94-ae2f-b38072bf5087"));
        user.setId(UUID.fromString("d445f4f5-0eda-4f7c-a36c-7ecc4ad4168a"));
        user.setName("user");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        taskService.createTask(new TaskCreateRequest("title", "description"), user.getId());

        verify(taskRepository).save(taskArgumentCaptor.capture());
        assertEquals(user, taskArgumentCaptor.getValue().getUser());
    }

    @Test
    void whenUserDoesNotExists_thenThrowRuntimeException() {
        UUID userId = UUID.fromString("bbcc4621-d88f-4a94-ae2f-b38072bf5087");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> taskService.createTask(new TaskCreateRequest("", ""), userId)
        );

        verify(userRepository).findById(userId);
    }

    @Test
    void whenFindUserTasks_thenReturnUserTasks() {
        UUID userId = UUID.fromString("bbcc4621-d88f-4a94-ae2f-b38072bf5087");
        List<Task> tasks = List.of(new Task(), new Task());
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            task.setTitle("task_%d".formatted(i));
        }
        Pageable pageable = PageRequest.of(0, tasks.size());
        when(taskRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)).thenReturn(new PageImpl<>(tasks));

        List<TaskResponse> tasksResponse = taskService.findUserTasks(userId, pageable);

        assertEquals(tasks.size(), tasksResponse.size());
        for (int i = 0; i < tasksResponse.size(); i++) {
            assertEquals(tasks.get(i).getTitle(), tasksResponse.get(i).title());
        }
    }

    @Test
    void whenUpdateRequestIsValid_thenUpdateTask() {
        UUID taskId = UUID.fromString("bbcc4621-d88f-4a94-ae2f-b38072bf5087");
        UUID userId = UUID.fromString("d445f4f5-0eda-4f7c-a36c-7ecc4ad4168a");
        var task = new Task();
        task.setTitle("title");
        task.setDescription("desc");
        when(taskRepository.findByIdAndUserId(taskId, userId)).thenReturn(Optional.of(task));

        taskService.updateTask(taskId, userId, new TaskUpdateRequest("new_title", "new_desc"));

        verify(taskRepository).findByIdAndUserId(taskId, userId);
        verify(taskRepository).save(task);
        assertAll(
                () -> assertEquals("new_title", task.getTitle()),
                () -> assertEquals("new_desc", task.getDescription())
        );
    }

    @Test
    void whenTaskIsNotPresent_thenDoNothing() {
        UUID taskId = UUID.fromString("bbcc4621-d88f-4a94-ae2f-b38072bf5087");
        UUID userId = UUID.fromString("d445f4f5-0eda-4f7c-a36c-7ecc4ad4168a");
        when(taskRepository.findByIdAndUserId(taskId, userId)).thenReturn(Optional.empty());

        taskService.updateTask(taskId, userId, new TaskUpdateRequest("", ""));

        verify(taskRepository).findByIdAndUserId(taskId, userId);
        verify(taskRepository, never()).save(any());
    }
}