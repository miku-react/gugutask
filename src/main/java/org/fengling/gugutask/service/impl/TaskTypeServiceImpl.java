package org.fengling.gugutask.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fengling.gugutask.dao.TaskTypeMapper;
import org.fengling.gugutask.pojo.TaskType;
import org.fengling.gugutask.service.TaskTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskTypeServiceImpl extends ServiceImpl<TaskTypeMapper, TaskType> implements TaskTypeService {
    @Autowired
    TaskTypeMapper taskTypeMapper;

    // 可以在这里实现额外的业务逻辑
    @Override
    public List<TaskType> findTaskTypesByUserId(Long userId) {
        return baseMapper.findTaskTypesByUserId(userId);
    }

    @Override
    public void createDefaultTaskTypesForUser(Long userId) {
        // 插入默认类型：工作、生活、真的特别
        taskTypeMapper.insert(new TaskType("工作", userId));
        taskTypeMapper.insert(new TaskType("生活", userId));
        taskTypeMapper.insert(new TaskType("真的特别", userId));
    }

    @Override
    public void removeByUserId(Long userId) {
        taskTypeMapper.deleteByUserId(userId);
    }


    @Override
    public Page<TaskType> findPagedTaskTypesByUserId(Long userId, Page<TaskType> page) {
        QueryWrapper<TaskType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }
}

