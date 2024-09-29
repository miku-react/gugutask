package org.fengling.gugutask.service.impl;

import org.fengling.gugutask.dao.UserRoleMapper;
import org.fengling.gugutask.pojo.UserRole;
import org.fengling.gugutask.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void save(UserRole userRole) {
        userRoleMapper.saveUserRole(userRole);
    }

    // 根据 userId 删除 user_roles 中的关联记录
    @Override
    public void removeByUserId(Long userId) {
        userRoleMapper.deleteByUserId(userId);
    }
}
