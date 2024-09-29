package org.fengling.gugutask.controller;

import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 获取当前用户信息 (用户自己)
    @GetMapping("/me")
    public User getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        String username = jwtUtil.extractUsername(token);  // 从JWT中提取用户名

        return userService.findByUsername(username);
    }

    // 更新用户自己的信息
    @PutMapping("/me")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authHeader, @RequestBody User user) {
        // 提取JWT token
        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token); // 从JWT中提取用户ID

        // 查找当前用户
        User currentUser = userService.findByUserId(userId);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // 检查用户名是否存在且不属于当前用户
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null && !existingUser.getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken");
        }

        // 保持用户ID一致
        user.setId(currentUser.getId());

        // 检查并处理用户名
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            user.setUsername(currentUser.getUsername());
        }

        // 检查并处理密码
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(currentUser.getPassword()); // 如果没有新密码，保持旧密码
        } else {
            // 对密码进行加密
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }

        // 检查并处理邮箱
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            user.setEmail(currentUser.getEmail());
        }

        // 更新用户信息
        userService.updateById(user);

        // 返回更新后的用户信息
        return ResponseEntity.ok(user);
    }

    // 用户注销自己
    @DeleteMapping("/me")
    public String deleteCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        String username = jwtUtil.extractUsername(token);  // 从JWT中提取用户名

        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            userService.deleteUserById(currentUser.getId());
            return "User account deleted successfully.";
        }
        return "User not found.";
    }
}
