package org.fengling.gugutask.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.fengling.gugutask.pojo.TaskTag;
import org.fengling.gugutask.service.TagService;
import org.fengling.gugutask.service.TaskService;
import org.fengling.gugutask.service.TaskTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-tags")
public class TaskTagController {

    @Autowired
    private TaskTagService taskTagService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TagService tagService;

    // 查询某个任务的所有标签
    @GetMapping("/task/{taskId}")
    public List<TaskTag> getTagsByTaskId(@PathVariable Long taskId, @RequestParam Long userId) {
        // 校验任务是否属于该用户
        if (!taskService.getById(taskId).getUserId().equals(userId)) {
            throw new RuntimeException("任务不属于该用户");
        }
        return taskTagService.findTagsByTaskId(taskId);
    }

    // 查询某个标签下的所有任务，校验 userId
    @GetMapping("/tag/{tagId}")
    public List<TaskTag> getTasksByTagId(@PathVariable Long tagId, @RequestParam Long userId) {
        // 校验标签是否属于该用户
        if (!tagService.getById(tagId).getUserId().equals(userId)) {
            throw new RuntimeException("标签不属于该用户");
        }
        return taskTagService.findTasksByTagId(tagId);
    }

    // 给任务添加标签，校验 userId
    @PostMapping
    public boolean addTaskTag(@RequestBody TaskTag taskTag, @RequestParam Long userId) {
        // 校验任务和标签是否属于该用户
        if (!taskService.getById(taskTag.getTaskId()).getUserId().equals(userId) ||
                !tagService.getById(taskTag.getTagId()).getUserId().equals(userId)) {
            throw new RuntimeException("任务或标签不属于该用户");
        }
        return taskTagService.save(taskTag);
    }

    // 删除任务的某个标签，校验 userId
    @DeleteMapping("/{taskId}/{tagId}")
    public boolean deleteTaskTag(@PathVariable Long taskId, @PathVariable Long tagId, @RequestParam Long userId) {
        // 校验任务和标签是否属于该用户
        if (!taskService.getById(taskId).getUserId().equals(userId) ||
                !tagService.getById(tagId).getUserId().equals(userId)) {
            throw new RuntimeException("任务或标签不属于该用户");
        }
        return taskTagService.remove(new QueryWrapper<TaskTag>().eq("task_id", taskId).eq("tag_id", tagId));
    }
}
