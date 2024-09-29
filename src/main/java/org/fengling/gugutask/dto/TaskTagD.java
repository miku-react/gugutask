package org.fengling.gugutask.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TaskTagD {
    private Long taskId;
    private Long tagId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public TaskTagD() {
    }

    public TaskTagD(Long taskId, Long tagId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.taskId = taskId;
        this.tagId = tagId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
