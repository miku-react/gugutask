package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.fengling.gugutask.pojo.TaskType;

import java.util.List;

@Mapper
public interface TaskTypeMapper extends BaseMapper<TaskType> {
    // 在这里添加额外的自定义查询方法
    // 根据 userId 查找 task_types 中的记录
    @Select("SELECT * FROM task_types WHERE user_id = #{userId}")
    List<TaskType> findTaskTypesByUserId(Long userId);

    // 根据 userId 删除 task_types 中的记录
    @Delete("DELETE FROM task_types WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") Long userId);

}
