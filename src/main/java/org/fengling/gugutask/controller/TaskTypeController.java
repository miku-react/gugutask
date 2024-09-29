package org.fengling.gugutask.controller;

import org.fengling.gugutask.dto.TaskTypeD;
import org.fengling.gugutask.pojo.TaskType;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.TaskTypeService;
import org.fengling.gugutask.util.R;
import org.fengling.gugutask.util.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task-types")
public class TaskTypeController {

    @Autowired
    SnowflakeIdGenerator snowflakeIdGenerator;
    @Autowired
    private TaskTypeService taskTypeService;
    @Autowired
    private JwtUtil jwtUtil;


    // 创建新的任务类型，并设置userId
    @PostMapping
    public R<String> createTaskType(@RequestBody TaskType taskType, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId
        // 设置userId
        taskType.setUserId(userId);
        //设置任务ID
        taskType.setId(snowflakeIdGenerator.generateId());
        taskTypeService.save(taskType);
        return R.success("Task type created successfully!");
    }

    // 更新任务类型，确保只能更新属于该用户的任务类型
    @PutMapping("/{id}")
    public R<String> updateTaskType(@PathVariable Long id, @RequestBody TaskType taskType, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        TaskType existingTaskType = taskTypeService.getById(id);
        if (existingTaskType != null && existingTaskType.getUserId() != null && existingTaskType.getUserId().equals(userId)) {
            taskType.setId(id);
            taskType.setUserId(userId);  // 确保userId保持一致
            taskTypeService.updateById(taskType);
            return R.success("Task type updated successfully!");
        }
        return R.forbidden("You are not allowed to update this task type.");
    }

    // 删除任务类型，确保只能删除属于该用户的任务类型
    @DeleteMapping("/{id}")
    public R<String> deleteTaskType(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        TaskType existingTaskType = taskTypeService.getById(id);
        if (existingTaskType != null && existingTaskType.getUserId() != null && existingTaskType.getUserId().equals(userId)) {
            taskTypeService.removeById(id);
            return R.success("Task type deleted successfully!");
        }
        return R.forbidden("You are not allowed to delete this task type.");
    }

    // 按照 userId 查找所有任务类型
    @GetMapping("/mine")
    public R<List<TaskTypeD>> getTaskTypesByUserId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        List<TaskType> taskTypes = taskTypeService.findTaskTypesByUserId(userId);

        // 转换为 TaskTypeD
        List<TaskTypeD> taskTypeDList = taskTypes.stream().map(taskType ->
                new TaskTypeD(taskType.getTypeName(), taskType.getCreatedAt(), taskType.getUpdatedAt())
        ).collect(Collectors.toList());

        return R.success(taskTypeDList);  // 返回封装的成功数据
    }
}
