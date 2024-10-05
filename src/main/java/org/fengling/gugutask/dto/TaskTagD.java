package org.fengling.gugutask.dto;

import lombok.Data;

@Data
public class TaskTagD {
    private Long taskId;
    private Long tagId;

    public TaskTagD() {
    }

    public TaskTagD(Long taskId, Long tagId) {
        this.taskId = taskId;
        this.tagId = tagId;
    }
}
