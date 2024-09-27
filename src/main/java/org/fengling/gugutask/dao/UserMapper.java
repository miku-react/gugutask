package org.fengling.gugutask.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.fengling.gugutask.pojo.User;

import java.util.Set;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 通过用户名查找用户
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    // 通过用户ID查找用户角色
    @Select("SELECT r.role_name FROM user_roles ur JOIN roles r ON ur.role_id = r.id WHERE ur.user_id = #{userId}")
    Set<String> findRolesByUserId(Long userId);

    // 通过角色ID查找权限
    @Select("SELECT p.permission_name FROM role_permissions rp JOIN permissions p ON rp.permission_id = p.id WHERE rp.role_id = #{roleId}")
    Set<String> findPermissionsByRoleId(Long roleId);

    // 通过角色名称查找角色ID
    @Select("SELECT id FROM roles WHERE role_name = #{roleName}")
    Long findRoleIdByRoleName(String roleName);
}
