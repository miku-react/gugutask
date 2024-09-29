package org.fengling.gugutask.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.fengling.gugutask.pojo.Role;

@Mapper
public interface RoleMapper {
    // 查找USER角色的ID
    @Select("SELECT * FROM roles WHERE role_name = #{roleName}")
    Role findByRoleName(@Param("roleName") String roleName);
}
