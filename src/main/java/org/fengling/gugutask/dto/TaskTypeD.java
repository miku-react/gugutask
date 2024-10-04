package org.fengling.gugutask.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskTypeD {
    private Long taskTypeId;
    private String typeName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public TaskTypeD() {
    }

    public TaskTypeD(String typeName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.typeName = typeName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public TaskTypeD(Long taskTypeId, String typeName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.taskTypeId = taskTypeId;
        this.typeName = typeName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }


}
