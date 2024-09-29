package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.fengling.gugutask.pojo.TaskType;

import java.util.List;

@Mapper
public interface TaskTypeMapper extends BaseMapper<TaskType> {
    // 在这里添加额外的自定义查询方法
    @Select("SELECT * FROM task_types WHERE user_id = #{userId}")
    List<TaskType> findTaskTypesByUserId(Long userId);

}
