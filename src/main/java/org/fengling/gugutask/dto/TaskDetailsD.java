package org.fengling.gugutask.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDetailsD {
    private Long taskId;
    private String taskName;
    private String taskDetail;
    private String taskPriority;
    private Date date1;
    private Time date2;
    private String taskStatus;
    private List<TagD> tags;
    private TaskTypeD taskType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public TaskDetailsD() {
    }

    public TaskDetailsD(Long taskId, String taskName, String taskDetail, String taskPriority, Date date1, Time date2, String taskStatus, List<TagD> tags, TaskTypeD taskType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDetail = taskDetail;
        this.taskPriority = taskPriority;
        this.date1 = date1;
        this.date2 = date2;
        this.taskStatus = taskStatus;
        this.tags = tags;
        this.taskType = taskType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
