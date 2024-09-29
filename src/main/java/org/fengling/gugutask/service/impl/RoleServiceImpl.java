package org.fengling.gugutask.service.impl;

import org.fengling.gugutask.dao.RoleMapper;
import org.fengling.gugutask.pojo.Role;
import org.fengling.gugutask.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role findByRoleName(String roleName) {
        return roleMapper.findByRoleName(roleName);
    }
}
