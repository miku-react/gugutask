package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.fengling.gugutask.pojo.TaskTag;

@Mapper
public interface TaskTagMapper extends BaseMapper<TaskTag> {
    // 可以在这里添加自定义的SQL查询方法
}
