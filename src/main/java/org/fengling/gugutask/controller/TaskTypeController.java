package org.fengling.gugutask.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.fengling.gugutask.dto.TaskTypeD;
import org.fengling.gugutask.pojo.Task;
import org.fengling.gugutask.pojo.TaskType;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.TaskService;
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
    TaskService taskService;
    @Autowired
    private TaskTypeService taskTypeService;
    @Autowired
    private JwtUtil jwtUtil;

    // 创建新的任务类型，并设置userId
    @PostMapping
    public R<TaskTypeD> createTaskType(@RequestBody TaskType taskType, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId
        // 设置userId
        taskType.setUserId(userId);
        //设置任务ID
        taskType.setId(snowflakeIdGenerator.generateId());
        taskTypeService.save(taskType);
        TaskTypeD taskTypeD = new TaskTypeD(
                taskType.getId(), taskType.getTypeName(), taskType.getCreatedAt(), taskType.getUpdatedAt()
        );
        return R.success(taskTypeD);
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
            return R.success("更新~成功！");
        }
        return R.forbidden("Token出问题了哦？没有这个用户");
    }

    // 删除任务类型，确保只能删除属于该用户的任务类型
    @DeleteMapping("/{id}")
    public R<String> deleteTaskType(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        TaskType existingTaskType = taskTypeService.getById(id);
        if (existingTaskType != null && existingTaskType.getUserId() != null && existingTaskType.getUserId().equals(userId)) {
            // 先查找所有属于该任务类型的任务
            List<Task> relatedTasks = taskService.list(new QueryWrapper<Task>().eq("type_id", id));

            // 删除找到的所有任务
            if (!relatedTasks.isEmpty()) {
                for (Task task : relatedTasks) {
                    taskService.removeById(task.getId());
                }
            }

            // 删除任务类型
            taskTypeService.removeById(id);
            return R.success("任务和任务类型删除~成功！");
        }
        return R.forbidden("Token出问题了哦？没有这个用户");
    }


    // 按照 userId 查找所有任务类型
    @GetMapping("/mine")
    public R<List<TaskTypeD>> getTaskTypesByUserId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        List<TaskType> taskTypes = taskTypeService.findTaskTypesByUserId(userId);

        // 转换为 TaskTypeD
        List<TaskTypeD> taskTypeDList = taskTypes.stream().map(taskType ->
                new TaskTypeD(taskType.getId(), taskType.getTypeName(), taskType.getCreatedAt(), taskType.getUpdatedAt())
        ).collect(Collectors.toList());

        return R.success(taskTypeDList);  // 返回封装的成功数据
    }
}
