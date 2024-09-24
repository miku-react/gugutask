package org.fengling.gugutask.controller;

import org.fengling.gugutask.pojo.TaskType;
import org.fengling.gugutask.service.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-types")
public class TaskTypeController {

    @Autowired
    private TaskTypeService taskTypeService;

    // 查询所有任务类型
    @GetMapping
    public List<TaskType> getAllTaskTypes() {
        return taskTypeService.list();
    }

    // 根据ID查询任务类型，校验是否属于该用户
    @GetMapping("/{id}")
    public TaskType getTaskTypeById(@PathVariable Long id, @RequestParam Long userId) {
        TaskType taskType = taskTypeService.getById(id);
        if (taskType != null && taskType.getUserId() != null && taskType.getUserId().equals(userId)) {
            return taskType;
        }
        return null;
    }

    // 创建新的任务类型，并设置userId
    @PostMapping
    public String createTaskType(@RequestBody TaskType taskType, @RequestParam Long userId) {
        taskType.setUserId(userId); // 设置userId
        taskTypeService.save(taskType);
        return "Task type created successfully!";
    }

    // 更新任务类型，确保只能更新属于该用户的任务类型
    @PutMapping("/{id}")
    public String updateTaskType(@PathVariable Long id, @RequestBody TaskType taskType, @RequestParam Long userId) {
        TaskType existingTaskType = taskTypeService.getById(id);
        if (existingTaskType != null && existingTaskType.getUserId() != null && existingTaskType.getUserId().equals(userId)) {
            taskType.setId(id);
            taskType.setUserId(userId); // 确保userId保持一致
            taskTypeService.updateById(taskType);
            return "Task type updated successfully!";
        }
        return "You are not allowed to update this task type.";
    }

    // 删除任务类型，确保只能删除属于该用户的任务类型
    @DeleteMapping("/{id}")
    public String deleteTaskType(@PathVariable Long id, @RequestParam Long userId) {
        TaskType existingTaskType = taskTypeService.getById(id);
        if (existingTaskType != null && existingTaskType.getUserId() != null && existingTaskType.getUserId().equals(userId)) {
            taskTypeService.removeById(id);
            return "Task type deleted successfully!";
        }
        return "You are not allowed to delete this task type.";
    }

    // 按照 userId 查找所有任务类型
    @GetMapping("/user/{userId}")
    public List<TaskType> getTaskTypesByUserId(@PathVariable Long userId) {
        return taskTypeService.findTaskTypesByUserId(userId);
    }
}
