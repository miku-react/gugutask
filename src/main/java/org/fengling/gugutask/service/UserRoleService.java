package org.fengling.gugutask.service;

import org.fengling.gugutask.pojo.UserRole;

public interface UserRoleService {
    void save(UserRole userRole);

    // 新增方法，根据 userId 删除 user_roles 中的关联记录
    void removeByUserId(Long userId);
}
