package org.fengling.gugutask.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("task_tags")
public class TaskTag {
    @TableField("task_id")
    private Long taskId;

    @TableField("tag_id")
    private Long tagId;

    private String createdAt;
    private String updatedAt;
}
