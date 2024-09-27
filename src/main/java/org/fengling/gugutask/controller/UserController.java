package org.fengling.gugutask.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户注册
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    // 获取当前用户信息 (用户自己)
    @RequiresAuthentication
    @GetMapping("/me")
    public User getCurrentUser(@RequestParam String username) {
        return userService.findByUsername(username); // 这里假设从session或token中获取username
    }

    // 更新用户自己的信息
    @RequiresAuthentication
    @PutMapping("/me")
    public User updateUser(@RequestParam String username, @RequestBody User user) {
        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            user.setId(currentUser.getId());
            userService.updateById(user);
        }
        return user;
    }

    // 用户注销自己
    @RequiresAuthentication
    @DeleteMapping("/me")
    public String deleteCurrentUser(@RequestParam String username) {
        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            userService.removeById(currentUser.getId());
        }
        return "User account deleted successfully.";
    }
}
