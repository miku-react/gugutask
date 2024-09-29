package org.fengling.gugutask.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.fengling.gugutask.pojo.Tag;
import org.fengling.gugutask.pojo.Task;
import org.fengling.gugutask.pojo.TaskTag;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.TagService;
import org.fengling.gugutask.service.TaskService;
import org.fengling.gugutask.service.TaskTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task-tags")
public class TaskTagController {

    @Autowired
    private TaskTagService taskTagService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TagService tagService;

    @Autowired
    private JwtUtil jwtUtil;

    // 查询某个任务的所有标签
    @GetMapping("/task/{taskId}")
    public List<TaskTag> getTagsByTaskId(@PathVariable Long taskId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        // 校验任务是否属于该用户
        if (!taskService.getById(taskId).getUserId().equals(userId)) {
            throw new RuntimeException("任务不属于该用户");
        }
        return taskTagService.findTagsByTaskId(taskId);
    }

    // 查询某个标签下的所有任务，校验 userId
    @GetMapping("/tag/{tagId}")
    public List<Task> getTasksByTagId(@PathVariable Long tagId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        // 查询标签并检查是否为 null
        Tag tag = tagService.getById(tagId);
        if (tag == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "标签不存在");
        }
        // 根据tagId查询任务标签映射
        List<TaskTag> taskTags = taskTagService.findTasksByTagId(tagId);

        // 根据taskId查询对应的Task实体
        List<Task> tasks = taskTags.stream()
                .map(taskTag -> taskService.getById(taskTag.getTaskId()))  // 根据taskId获取Task对象
                .filter(Objects::nonNull)  // 过滤掉可能为null的任务
                .collect(Collectors.toList());

        return tasks;

    }

    // 给任务添加标签，校验 userId
    @PostMapping
    public boolean addTaskTag(@RequestBody TaskTag taskTag, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        // 校验任务和标签是否属于该用户
        if (!taskService.getById(taskTag.getTaskId()).getUserId().equals(userId) ||
                !tagService.getById(taskTag.getTagId()).getUserId().equals(userId)) {
            throw new RuntimeException("任务或标签不属于该用户");
        }
        return taskTagService.save(taskTag);
    }

    // 删除任务的某个标签，校验 userId
    @DeleteMapping("/{taskId}/{tagId}")
    public boolean deleteTaskTag(@PathVariable Long taskId, @PathVariable Long tagId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        // 校验任务和标签是否属于该用户
        if (!taskService.getById(taskId).getUserId().equals(userId) ||
                !tagService.getById(tagId).getUserId().equals(userId)) {
            throw new RuntimeException("任务或标签不属于该用户");
        }
        return taskTagService.remove(new QueryWrapper<TaskTag>().eq("task_id", taskId).eq("tag_id", tagId));
    }
}
