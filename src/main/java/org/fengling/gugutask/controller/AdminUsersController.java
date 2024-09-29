package org.fengling.gugutask.controller;

import org.fengling.gugutask.dto.UserD;
import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.service.UserService;
import org.fengling.gugutask.util.R;
import org.fengling.gugutask.util.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUsersController {
    @Autowired
    SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 获取所有用户 (仅管理员可访问)
    @GetMapping
    public R<List<UserD>> getAllUsers() {
        List<User> users = userService.list();

        // 将 User 转换为 UserD
        List<UserD> userDList = users.stream()
                .map(user -> new UserD(user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt()))
                .collect(Collectors.toList());

        return R.success(userDList);
    }

    // 获取单个用户 (仅管理员可访问)
    @GetMapping("/{id}")
    public R<UserD> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return R.notFound("用户未找到");
        }

        // 封装为 UserD 返回
        UserD userD = new UserD(user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
        return R.success(userD);
    }

    // 创建用户 (仅管理员可访问)
    @PostMapping
    public R<String> createUser(@RequestBody User user) {
        // 检查用户名是否存在
        if (userService.findByUsername(user.getUsername()) != null) {
            return R.error("用户名已存在");
        }

        // 对密码进行加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }

        // 生成用户ID
        user.setId(snowflakeIdGenerator.generateId());
        userService.save(user);

        // 返回成功响应
        return R.success("用户创建成功");
    }

    // 更新用户 (仅管理员可访问)
    @PutMapping("/{id}")
    public R<UserD> updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            return R.notFound("用户未找到");
        }

        // 检查用户名是否存在且不属于当前用户
        User userWithSameUsername = userService.findByUsername(user.getUsername());
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(id)) {
            return R.error("用户名已被使用");
        }

        // 保持用户ID一致
        user.setId(id);

        // 检查并处理用户名
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            user.setUsername(existingUser.getUsername());
        }

        // 检查并处理密码
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword()); // 如果没有新密码，保持旧密码
        } else {
            // 对密码进行加密
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }

        // 检查并处理邮箱
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            user.setEmail(existingUser.getEmail());
        }

        // 更新用户信息
        userService.updateById(user);

        // 封装为 UserD 返回
        UserD userD = new UserD(user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
        return R.success(userD);
    }

    // 删除用户 (仅管理员可访问)
    @DeleteMapping("/{id}")
    public R<String> deleteUser(@PathVariable Long id) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            return R.notFound("用户未找到");
        }
        userService.deleteUserById(id);
        return R.success("用户删除成功");
    }
}
