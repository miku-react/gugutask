package org.fengling.gugutask.controller;

import org.fengling.gugutask.pojo.Task;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final JwtUtil jwtUtil;

    public TaskController(TaskService taskService, JwtUtil jwtUtil) {
        this.taskService = taskService;
        this.jwtUtil = jwtUtil;
    }

    // 查询单个任务 (用户只能查询自己的任务)
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        Task task = taskService.getById(id);
        return (task != null && task.getUserId().equals(userId)) ? task : null;
    }

    // 根据用户ID查询任务
    @GetMapping("/user")
    public List<Task> getTaskByUserId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        return taskService.getTasksByUserId(userId);
    }

    // 创建任务
    @PostMapping
    public void createTask(@RequestBody Task task, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        task.setUserId(userId);  // 设置任务的userId
        taskService.save(task);
    }

    // 更新任务
    @PutMapping("/{id}")
    public boolean updateTask(@PathVariable Long id, @RequestBody Task task, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        Task existingTask = taskService.getById(id);
        if (existingTask != null && existingTask.getUserId().equals(userId)) {
            task.setId(id);
            task.setUserId(userId);  // 确保任务的userId不被更改
            return taskService.updateById(task);
        }
        return false;
    }

    // 删除任务
    @DeleteMapping("/{id}")
    public boolean deleteTask(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        Task existingTask = taskService.getById(id);
        if (existingTask != null && existingTask.getUserId().equals(userId)) {
            return taskService.removeById(id);
        }
        return false;
    }
}
