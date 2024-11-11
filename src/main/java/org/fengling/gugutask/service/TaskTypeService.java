package org.fengling.gugutask.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.fengling.gugutask.pojo.TaskType;

import java.util.List;

public interface TaskTypeService extends IService<TaskType> {
    // 可以在这里添加额外的业务逻辑方法
    List<TaskType> findTaskTypesByUserId(Long userId);

    // 给每个用户创建默认类型
    void createDefaultTaskTypesForUser(Long userId);

    void removeByUserId(Long userId);

    Page<TaskType> findPagedTaskTypesByUserId(Long userId, Page<TaskType> page);

    boolean existsByUserIdAndTypeName(Long userId, String typeName); // 自定义方法

}
