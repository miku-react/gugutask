package org.fengling.gugutask.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.fengling.gugutask.pojo.Role;

@Mapper
public interface RoleMapper {
    @Select("SELECT * FROM roles WHERE role_name = #{roleName}")
    Role findByRoleName(@Param("roleName") String roleName);
}
