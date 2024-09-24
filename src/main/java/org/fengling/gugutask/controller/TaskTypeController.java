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

    // 根据ID查询任务类型
    @GetMapping("/{id}")
    public TaskType getTaskTypeById(@PathVariable Long id) {
        return taskTypeService.getById(id);
    }

    // 创建新的任务类型
    @PostMapping
    public String createTaskType(@RequestBody TaskType taskType) {
        taskTypeService.save(taskType);
        return "Task type created successfully!";
    }

    // 更新任务类型
    @PutMapping("/{id}")
    public String updateTaskType(@PathVariable Long id, @RequestBody TaskType taskType) {
        taskType.setId(id);
        taskTypeService.updateById(taskType);
        return "Task type updated successfully!";
    }

    // 删除任务类型
    @DeleteMapping("/{id}")
    public String deleteTaskType(@PathVariable Long id) {
        taskTypeService.removeById(id);
        return "Task type deleted successfully!";
    }
}
