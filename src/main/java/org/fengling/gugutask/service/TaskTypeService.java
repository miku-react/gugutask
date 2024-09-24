package org.fengling.gugutask.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fengling.gugutask.pojo.TaskType;

import java.util.List;

public interface TaskTypeService extends IService<TaskType> {
    // 可以在这里添加额外的业务逻辑方法
    List<TaskType> findTaskTypesByUserId(Long userId);
}
