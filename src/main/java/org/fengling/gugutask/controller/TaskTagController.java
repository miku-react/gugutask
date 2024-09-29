package org.fengling.gugutask.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.fengling.gugutask.dto.TagD;
import org.fengling.gugutask.dto.TaskD;
import org.fengling.gugutask.dto.TaskTypeD;
import org.fengling.gugutask.pojo.Tag;
import org.fengling.gugutask.pojo.Task;
import org.fengling.gugutask.pojo.TaskTag;
import org.fengling.gugutask.pojo.TaskType;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.TagService;
import org.fengling.gugutask.service.TaskService;
import org.fengling.gugutask.service.TaskTagService;
import org.fengling.gugutask.service.TaskTypeService;
import org.fengling.gugutask.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    private TaskTypeService taskTypeService;

    @Autowired
    private JwtUtil jwtUtil;

    // 查询某个任务的所有标签，并返回封装的 TagD DTO
    @GetMapping("/task/{taskId}")
    public R<List<TagD>> getTagsByTaskId(@PathVariable Long taskId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        // 校验任务是否属于该用户
        Task task = taskService.getById(taskId);
        if (task == null || !task.getUserId().equals(userId)) {
            return R.forbidden("任务不属于该用户");
        }

        List<TaskTag> taskTags = taskTagService.findTagsByTaskId(taskId);

        // 将 TaskTag 转换为 TagD DTO
        List<TagD> tagDList = taskTags.stream().map(taskTag -> {
            Tag tag = tagService.getById(taskTag.getTagId());
            return new TagD(
                    tag.getTagName(),
                    tag.getCreatedAt(),
                    tag.getUpdatedAt()
            );
        }).collect(Collectors.toList());

        return R.success(tagDList);
    }

    // 查询某个标签下的所有任务，并返回封装的 TaskD DTO
    @GetMapping("/tag/{tagId}")
    public R<List<TaskD>> getTasksByTagId(@PathVariable Long tagId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        // 校验标签是否属于该用户
        Tag tag = tagService.getById(tagId);
        if (tag == null || !tag.getUserId().equals(userId)) {
            return R.notFound("标签不存在或不属于该用户");
        }

        List<TaskTag> taskTags = taskTagService.findTasksByTagId(tagId);

        // 将 TaskTag 转换为 TaskD DTO
        List<TaskD> taskDList = taskTags.stream().map(taskTag -> {
            Task task = taskService.getById(taskTag.getTaskId());

            // 获取任务的类型
            TaskType taskType = taskTypeService.getById(task.getTypeId());
            TaskTypeD taskTypeD = new TaskTypeD(
                    taskType.getTypeName(),
                    taskType.getCreatedAt(),
                    taskType.getUpdatedAt()
            );

            // 获取任务的标签
            List<TaskTag> taskTagsForTask = taskTagService.findTagsByTaskId(task.getId());
            List<TagD> tagDList = taskTagsForTask.stream().map(taskTagForTask -> {
                Tag tagForTask = tagService.getById(taskTagForTask.getTagId());
                return new TagD(
                        tagForTask.getTagName(),
                        tagForTask.getCreatedAt(),
                        tagForTask.getUpdatedAt()
                );
            }).collect(Collectors.toList());

            // 封装 TaskD 对象
            return new TaskD(
                    task.getName(),
                    task.getDetail(),
                    task.getPriority(),
                    task.getDate1(),
                    task.getDate2(),
                    task.getStatus(),
                    tagDList,   // 设置任务标签
                    List.of(taskTypeD), // 设置任务类型
                    task.getCreatedAt(),
                    task.getUpdatedAt()
            );
        }).collect(Collectors.toList());

        return R.success(taskDList);
    }

    // 给任务添加标签，校验 userId
    @PostMapping
    public R<String> addTaskTag(@RequestBody TaskTag taskTag, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        // 校验任务和标签是否属于该用户
        if (!taskService.getById(taskTag.getTaskId()).getUserId().equals(userId) ||
                !tagService.getById(taskTag.getTagId()).getUserId().equals(userId)) {
            return R.forbidden("任务或标签不属于该用户");
        }

        taskTagService.save(taskTag);
        return R.success("任务标签添加成功");
    }

    // 删除任务的某个标签，校验 userId
    @DeleteMapping("/{taskId}/{tagId}")
    public R<String> deleteTaskTag(@PathVariable Long taskId, @PathVariable Long tagId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        // 校验任务和标签是否属于该用户
        if (!taskService.getById(taskId).getUserId().equals(userId) ||
                !tagService.getById(tagId).getUserId().equals(userId)) {
            return R.forbidden("任务或标签不属于该用户");
        }

        taskTagService.removeById(new QueryWrapper<TaskTag>().eq("task_id", taskId).eq("tag_id", tagId));
        return R.success("任务标签删除成功");
    }
}
