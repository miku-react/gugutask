package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.fengling.gugutask.pojo.Tag;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    // 自定义查询方法，按 userId 查询所有 Tag
    @Select("SELECT * FROM tags WHERE user_id = #{userId}")
    List<Tag> findTagsByUserId(Long userId);
}
