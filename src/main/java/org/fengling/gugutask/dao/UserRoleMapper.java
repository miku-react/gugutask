package org.fengling.gugutask.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.fengling.gugutask.pojo.UserRole;

@Mapper
public interface UserRoleMapper {
    @Insert("INSERT INTO user_roles(user_id, role_id) VALUES(#{userId}, #{roleId})")
    void saveUserRole(UserRole userRole);

    // 根据 userId 删除 user_roles 表中用户的记录
    @Delete("DELETE FROM user_roles WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") Long userId);
}
