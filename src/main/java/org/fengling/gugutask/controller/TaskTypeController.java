package org.fengling.gugutask.controller;

import org.fengling.gugutask.pojo.TaskType;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-types")
public class TaskTypeController {

    @Autowired
    private TaskTypeService taskTypeService;

    @Autowired
    private JwtUtil jwtUtil;

    // 根据ID查询任务类型，校验是否属于该用户
    @GetMapping("/mine")
    public List<TaskType> getTaskTypeById(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        List<TaskType> taskTypes = taskTypeService.findTaskTypesByUserId(userId);
        if (taskTypes != null) {
            return taskTypes;
        }
        return null;
    }

    // 创建新的任务类型，并设置userId
    @PostMapping
    public String createTaskType(@RequestBody TaskType taskType, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        taskType.setUserId(userId);  // 设置userId
        taskTypeService.save(taskType);
        return "Task type created successfully!";
    }

    // 更新任务类型，确保只能更新属于该用户的任务类型
    @PutMapping("/{id}")
    public String updateTaskType(@PathVariable Long id, @RequestBody TaskType taskType, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        TaskType existingTaskType = taskTypeService.getById(id);
        if (existingTaskType != null && existingTaskType.getUserId() != null && existingTaskType.getUserId().equals(userId)) {
            taskType.setId(id);
            taskType.setUserId(userId);  // 确保userId保持一致
            taskTypeService.updateById(taskType);
            return "Task type updated successfully!";
        }
        return "You are not allowed to update this task type.";
    }

    // 删除任务类型，确保只能删除属于该用户的任务类型
    @DeleteMapping("/{id}")
    public String deleteTaskType(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        TaskType existingTaskType = taskTypeService.getById(id);
        if (existingTaskType != null && existingTaskType.getUserId() != null && existingTaskType.getUserId().equals(userId)) {
            taskTypeService.removeById(id);
            return "Task type deleted successfully!";
        }
        return "You are not allowed to delete this task type.";
    }

    // 按照 userId 查找所有任务类型
    @GetMapping("/user")
    public List<TaskType> getTaskTypesByUserId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        return taskTypeService.findTaskTypesByUserId(userId);
    }
}
