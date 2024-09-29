package org.fengling.gugutask.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fengling.gugutask.dao.TaskTagMapper;
import org.fengling.gugutask.pojo.TaskTag;
import org.fengling.gugutask.service.TaskTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskTagServiceImpl extends ServiceImpl<TaskTagMapper, TaskTag> implements TaskTagService {

    @Autowired
    private TaskTagMapper taskTagMapper;

    @Override
    public List<TaskTag> findTagsByTaskId(Long taskId) {
        // 查询指定任务的所有标签
        return baseMapper.selectList(new QueryWrapper<TaskTag>().eq("task_id", taskId));
    }

    @Override
    public List<TaskTag> findTasksByTagId(Long tagId) {
        // 查询指定标签的所有任务
        return baseMapper.selectList(new QueryWrapper<TaskTag>().eq("tag_id", tagId));
    }

    @Override
    public void removeByUserId(Long userId) {
        taskTagMapper.deleteByUserId(userId);
    }
}
