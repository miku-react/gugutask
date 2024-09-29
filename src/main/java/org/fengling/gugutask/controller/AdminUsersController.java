package org.fengling.gugutask.controller;

import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.service.UserService;
import org.fengling.gugutask.util.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<User> getAllUsers() {
        return userService.list();
    }

    // 获取单个用户 (仅管理员可访问)

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    // 创建用户 (仅管理员可访问)
    @PostMapping
    public User createUser(@RequestBody User user) {
        // 对密码进行加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }

        // 生成用户ID
        user.setId(snowflakeIdGenerator.generateId());
        userService.save(user);
        return user;
    }

    // 更新用户 (仅管理员可访问)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // 检查用户名是否存在且不属于当前用户
        User userWithSameUsername = userService.findByUsername(user.getUsername());
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken");
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

        // 返回更新后的用户信息
        return ResponseEntity.ok(user);
    }


    // 删除用户 (仅管理员可访问)
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "User deleted successfully.";
    }
}
