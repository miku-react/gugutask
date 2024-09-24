package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.fengling.gugutask.pojo.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
