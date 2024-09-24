package org.fengling.gugutask.controller;

import org.fengling.gugutask.pojo.Task;
import org.fengling.gugutask.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.list();
    }

    @PostMapping
    public void createTask(@RequestBody Task task) {
        taskService.save(task);
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        taskService.updateById(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.removeById(id);
    }
}
