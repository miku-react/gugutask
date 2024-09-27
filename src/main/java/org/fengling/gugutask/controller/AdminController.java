package org.fengling.gugutask.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    @Autowired
    private UserService userService;

    // 获取所有用户 (仅管理员可访问)
    @RequiresRoles("admin")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.list();
    }

    // 获取单个用户 (仅管理员可访问)
    @RequiresRoles("admin")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    // 创建用户 (仅管理员可访问)
    @RequiresRoles("admin")
    @PostMapping
    public User createUser(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    // 更新用户 (仅管理员可访问)
    @RequiresRoles("admin")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateById(user);
        return user;
    }

    // 删除用户 (仅管理员可访问)
    @RequiresRoles("admin")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return "User deleted successfully.";
    }
}