package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.fengling.gugutask.pojo.Task;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    // 在这里添加自定义的SQL查询方法，MyBatis Plus会提供基本的CRUD方法
    // 根据 userId 查找 tasks 中的记录
    @Select("SELECT * FROM tasks WHERE user_id = #{userId}")
    List<Task> findTasksByUserId(@Param("userId") Long userId);

    // 根据 userId 删除 tasks 中的记录
    @Delete("DELETE FROM tasks WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") Long userId);

    // 根据 userId 和 typeId 查找 tasks 中的记录
    @Select("SELECT t.*, tt.id AS type_id, tt.type_name, tt.created_at AS type_created_at, tt.updated_at AS type_updated_at, " +
            "tag.id AS tag_id, tag.tag_name, tag.created_at AS tag_created_at, tag.updated_at AS tag_updated_at " +
            "FROM tasks t " +
            "LEFT JOIN task_types tt ON t.type_id = tt.id " +
            "LEFT JOIN task_tags ttg ON t.id = ttg.task_id " +
            "LEFT JOIN tags tag ON ttg.tag_id = tag.id " +
            "WHERE t.user_id = #{userId} AND t.type_id = #{typeId}")
    List<Map<String, Object>> getTasksWithDetailsByUserIdAndType(@Param("userId") Long userId, @Param("typeId") Long typeId);


    @Select("SELECT tag.id, tag.tag_name, tag.created_at, tag.updated_at " +
            "FROM tags tag " +
            "LEFT JOIN task_tags ttg ON tag.id = ttg.tag_id " +
            "WHERE ttg.task_id = #{taskId}")
    List<Map<String, Object>> findTagsByTaskId(Long taskId);
}
