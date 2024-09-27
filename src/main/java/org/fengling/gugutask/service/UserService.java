package org.fengling.gugutask.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fengling.gugutask.pojo.User;

import java.util.Set;

public interface UserService extends IService<User> {
    User findByUsername(String username);

    Set<String> findRolesByUserId(Long userId);

    Set<String> findPermissionsByRoleId(Long roleId);

    Long findRoleIdByRoleName(String roleName);
}
