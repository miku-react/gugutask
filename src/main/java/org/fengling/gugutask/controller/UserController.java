package org.fengling.gugutask.controller;

import org.fengling.gugutask.dto.UserD;
import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.UserService;
import org.fengling.gugutask.util.R;
import org.springframework.beans.factory.annotation.Autowired;
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
    public R<UserD> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        String username = jwtUtil.extractUsername(token);  // 从JWT中提取用户名

        User user = userService.findByUsername(username);

        // 转换 User 到 UserD
        if (user != null) {
            UserD userD = new UserD(
                    user.getUsername(),
                    user.getEmail(),
                    user.getCreatedAt(),
                    user.getUpdatedAt(),
                    user.getAvatar()
            );
            return R.success(userD);  // 返回封装的成功数据
        }

        // 如果用户未找到，返回404错误
        return R.notFound("用户没找到");
    }

    // 更新用户自己的信息
    @PutMapping("/me")
    public R<UserD> updateUser(@RequestHeader("Authorization") String authHeader, @RequestBody User user) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token); // 从JWT中提取用户ID

        User currentUser = userService.findByUserId(userId);
        if (currentUser == null) {
            return R.notFound("没有这个用户ID");
        }

        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null && !existingUser.getId().equals(currentUser.getId())) {
            return R.error("用户名已被注册啦");
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            return R.error("邮箱已被注册");
        }
        user.setId(currentUser.getId());

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            user.setUsername(currentUser.getUsername());
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(currentUser.getPassword());
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            user.setEmail(currentUser.getEmail());
        }

        userService.updateById(user);
        User userAfterUpdate = userService.findByUserId(userId);

        // 更新完成后返回新的用户信息，封装成 UserD
        UserD updatedUserD = new UserD(
                userAfterUpdate.getUsername(),
                userAfterUpdate.getEmail(),
                userAfterUpdate.getCreatedAt(),
                userAfterUpdate.getUpdatedAt(),
                userAfterUpdate.getAvatar()
        );

        return R.success(updatedUserD);  // 返回封装的成功数据
    }

    // 用户注销自己
    @DeleteMapping("/me")
    public R<String> deleteCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        String username = jwtUtil.extractUsername(token);  // 从JWT中提取用户名

        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            userService.deleteUserById(currentUser.getId());
            return R.success("有缘再见！");
        }
        return R.notFound("没有找到这个用户哦");
    }
}
