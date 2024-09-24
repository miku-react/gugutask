package org.fengling.gugutask.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fengling.gugutask.dao.TaskTypeMapper;
import org.fengling.gugutask.pojo.TaskType;
import org.fengling.gugutask.service.TaskTypeService;
import org.springframework.stereotype.Service;

@Service
public class TaskTypeServiceImpl extends ServiceImpl<TaskTypeMapper, TaskType> implements TaskTypeService {
    // 可以在这里实现额外的业务逻辑
}
