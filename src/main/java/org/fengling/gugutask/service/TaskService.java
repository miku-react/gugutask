package org.fengling.gugutask.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fengling.gugutask.pojo.Task;

import java.util.List;

public interface TaskService extends IService<Task> {
    // 可以在这里添加额外的业务逻辑方法
List<Task> getTasksByUserId(Long userId);
}
