package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.fengling.gugutask.pojo.TaskType;

@Mapper
public interface TaskTypeMapper extends BaseMapper<TaskType> {
    // 在这里添加额外的自定义查询方法（如果需要）
}
