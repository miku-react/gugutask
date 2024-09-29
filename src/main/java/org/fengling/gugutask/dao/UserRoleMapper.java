package org.fengling.gugutask.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.fengling.gugutask.pojo.UserRole;

@Mapper
public interface UserRoleMapper {
    @Insert("INSERT INTO user_roles(user_id, role_id) VALUES(#{userId}, #{roleId})")
    void saveUserRole(UserRole userRole);
}
