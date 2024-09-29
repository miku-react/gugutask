package org.fengling.gugutask.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("task_types")
public class TaskType {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String typeName;
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // 无参构造函数（MyBatis需要）
    public TaskType() {
    }

    // 带有typeName和userId的构造函数
    public TaskType(String typeName, Long userId) {
        this.typeName = typeName;
        this.userId = userId;

    }
}
