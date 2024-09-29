package org.fengling.gugutask.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fengling.gugutask.dao.UserMapper;
import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private TaskTypeService taskTypeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskTagService taskTagService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User findByUserId(Long userid) {
        return userMapper.findByUserId(userid);
    }

    @Override
    public List<String> findRolesByUserId(Long userId) {
        return userMapper.findRolesByUserId(userId);
    }

    // 实现获取用户角色和权限并转换为GrantedAuthority
    // 只转换角色为GrantedAuthority对象
    @Override
    public Set<GrantedAuthority> getUserAuthorities(Long userId) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        List<String> roles = findRolesByUserId(userId);

        // 将每个角色转换为GrantedAuthority，并加上"ROLE_"前缀
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return authorities;
    }

    @Override
    public void deleteUserById(Long userId) {
        // 1. 删除与该用户相关的任务标签（task_tags 表）
        taskTagService.removeByUserId(userId);

        // 2. 删除与该用户相关的任务（tasks 表）
        taskService.removeByUserId(userId);

        // 3. 删除与该用户相关的任务类型（task_types 表）
        taskTypeService.removeByUserId(userId);

        // 4. 删除与该用户相关的标签（tags 表）
        tagService.removeByUserId(userId);

        // 5. 删除用户的角色关联（user_roles 表）
        userRoleService.removeByUserId(userId);

        // 6. 最后删除用户（users 表）
        userMapper.deleteById(userId);
    }
}
