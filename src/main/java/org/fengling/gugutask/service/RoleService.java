package org.fengling.gugutask.service;

import org.fengling.gugutask.pojo.Role;

public interface RoleService {
    Role findByRoleName(String roleName);
}
