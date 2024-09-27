package org.fengling.gugutask.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fengling.gugutask.dao.UserMapper;
import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public Set<String> findRolesByUserId(Long userId) {
        return userMapper.findRolesByUserId(userId);
    }

    @Override
    public Set<String> findPermissionsByRoleId(Long roleId) {
        return userMapper.findPermissionsByRoleId(roleId);
    }

    @Override
    public Long findRoleIdByRoleName(String roleName) {
        return userMapper.findRoleIdByRoleName(roleName);
    }
}
