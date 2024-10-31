package org.sawyron.taskmaster.tasks.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest(@Size(max = 100) @NotBlank String title,
                                @Size(max = 512) @NotBlank String description) {
}
