package org.sawyron.taskmaster.tasks;

import jakarta.validation.Valid;
import org.sawyron.taskmaster.auth.UserDetailsAdapter;
import org.sawyron.taskmaster.tasks.dtos.TaskCreateRequest;
import org.sawyron.taskmaster.tasks.dtos.TaskResponse;
import org.sawyron.taskmaster.tasks.dtos.TaskUpdateRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestBody @Valid TaskCreateRequest request,
            @AuthenticationPrincipal UserDetailsAdapter userDetails
    ) {
        taskService.createTask(request, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAllTasks(
            @AuthenticationPrincipal UserDetailsAdapter userDetails,
            Pageable pageable
    ) {
        List<TaskResponse> tasks = taskService.findUserTasks(userDetails.getId(), pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable UUID id,
            @RequestBody @Valid TaskUpdateRequest request,
            @AuthenticationPrincipal UserDetailsAdapter userDetails
    ) {
        taskService.updateTask(id, userDetails.getId(), request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsAdapter userDetails
    ) {
        taskService.removeUserTask(id, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
