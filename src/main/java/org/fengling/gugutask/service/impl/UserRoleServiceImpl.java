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
}
