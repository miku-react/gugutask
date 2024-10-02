package org.fengling.gugutask.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskD {
    private Long Id;
    private String name;
    private String detail;
    private String priority;
    private String date1;
    private String date2;
    private String status;
    private List<TagD> tags;
    private List<TaskTypeD> taskTypes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public TaskD() {
    }


    public TaskD(Long Id, String name, String detail, String priority, String date1, String date2, String status, List<TagD> tags, List<TaskTypeD> taskTypes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.Id = Id;
        this.name = name;
        this.detail = detail;
        this.priority = priority;
        this.date1 = date1;
        this.date2 = date2;
        this.status = status;
        this.tags = tags;
        this.taskTypes = taskTypes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
