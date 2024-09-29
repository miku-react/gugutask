package org.fengling.gugutask.controller;

import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 获取当前用户信息 (用户自己)
    @GetMapping("/me")
    public User getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        String username = jwtUtil.extractUsername(token);  // 从JWT中提取用户名

        return userService.findByUsername(username);
    }

    // 更新用户自己的信息
    @PutMapping("/me")
    public User updateUser(@RequestHeader("Authorization") String authHeader, @RequestBody User user) {
        String token = authHeader.substring(7);  // 提取JWT token
        String username = jwtUtil.extractUsername(token);  // 从JWT中提取用户名

        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            user.setId(currentUser.getId());
            userService.updateById(user);
        }
        return user;
    }

    // 用户注销自己
    @DeleteMapping("/me")
    public String deleteCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        String username = jwtUtil.extractUsername(token);  // 从JWT中提取用户名

        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            userService.removeById(currentUser.getId());
            return "User account deleted successfully.";
        }
        return "User not found.";
    }
}
