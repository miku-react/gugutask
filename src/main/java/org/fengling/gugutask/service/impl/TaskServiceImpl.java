package org.fengling.gugutask.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fengling.gugutask.dao.TaskMapper;
import org.fengling.gugutask.pojo.Task;
import org.fengling.gugutask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    // 在这里重写Service层的业务逻辑
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<Task> getTasksByUserId(Long userId) {
        // 调用 TaskMapper 中的自定义查询方法
        return taskMapper.findTasksByUserId(userId);
    }

    @Override
    public void removeByUserId(Long userId) {
        taskMapper.deleteByUserId(userId);
    }
}
