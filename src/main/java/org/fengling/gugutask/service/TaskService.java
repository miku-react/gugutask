package org.fengling.gugutask.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.fengling.gugutask.dto.TaskDetailsD;
import org.fengling.gugutask.pojo.Task;

import java.util.List;

public interface TaskService extends IService<Task> {
    // 可以在这里添加额外的业务逻辑方法
    List<Task> getTasksByUserId(Long userId);

    // 根据 userId 删除 tasks 中的记录
    void removeByUserId(Long userId);

    List<TaskDetailsD> getTasksWithDetailsByUserId(Long userId);

    Page<TaskDetailsD> getTasksWithDetailsByUserIdAndTaskType(Long userId, Long taskType, int page, int size);
}
