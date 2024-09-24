package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.fengling.gugutask.pojo.Tag;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    // 自定义查询方法在这里添加
}
