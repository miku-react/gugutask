package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.fengling.gugutask.pojo.TaskTag;

@Mapper
public interface TaskTagMapper extends BaseMapper<TaskTag> {
    // 可以在这里添加自定义的SQL查询方法
    // 根据 userId 删除 task_tags 中的记录
    @Delete("DELETE FROM task_tags WHERE task_id IN (SELECT id FROM tasks WHERE user_id = #{userId})")
    void deleteByUserId(@Param("userId") Long userId);
}
