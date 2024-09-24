package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.fengling.gugutask.pojo.Task;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    // 在这里添加自定义的SQL查询方法，MyBatis Plus会提供基本的CRUD方法
    List<Task> findTasksByUserId(@Param("userId") Long userId);
}
