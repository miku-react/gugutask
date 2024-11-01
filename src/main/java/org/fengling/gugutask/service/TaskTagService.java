package org.fengling.gugutask.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fengling.gugutask.pojo.TaskTag;

import java.util.List;

public interface TaskTagService extends IService<TaskTag> {
    // 定义通过 taskId 查询所有标签的方法
    List<TaskTag> findTagsByTaskId(Long taskId);

    // 定义通过 tagId 查询所有任务的方法
    List<TaskTag> findTasksByTagId(Long tagId);

    // 根据 userId 删除 task_tags 中的记录
    void removeByUserId(Long userId);

    // 根据 TaskId 删除 task_tags 中的记录
    void deleteByTaskId(Long taskId);
}
