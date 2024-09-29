package org.fengling.gugutask.controller;

import org.fengling.gugutask.pojo.Role;
import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.pojo.UserRole;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.RoleService;
import org.fengling.gugutask.service.TaskTypeService;
import org.fengling.gugutask.service.UserRoleService;
import org.fengling.gugutask.service.UserService;
import org.fengling.gugutask.util.R;
import org.fengling.gugutask.util.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    SnowflakeIdGenerator snowflakeIdGenerator;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private TaskTypeService taskTypeService;

    // 登录接口
    @PostMapping("/login")
    public R<Map<String, String>> login(@RequestBody User loginRequest) {
        // 从数据库查找用户
        User user = userService.findByUsername(loginRequest.getUsername());

        // 验证用户名是否存在和密码是否正确
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return R.unauthorized("用户名或密码错误");
        }

        // 获取用户的角色信息（使用findRolesByUserId）
        List<String> roles = userService.findRolesByUserId(user.getId());

        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), roles);

        // 返回token给客户端
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return R.success(response);
    }

    // 注册接口
    @PostMapping("/register")
    public R<String> registerUser(@RequestBody User user) {
        // 检查用户名是否存在
        if (userService.findByUsername(user.getUsername()) != null) {
            return R.error("用户名已被注册");
        }

        // 生成用户ID
        user.setId(snowflakeIdGenerator.generateId());

        // 对密码进行加密
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // 保存用户到数据库
        userService.save(user); // 保存后 user 会有 id

        // 查找USER角色的ID
        Role userRole = roleService.findByRoleName("USER");
        if (userRole == null) {
            return R.error("USER角色未找到");
        }

        // 将用户角色关联到user_roles表中
        UserRole userRoleAssociation = new UserRole(user.getId(), userRole.getId());
        userRoleService.save(userRoleAssociation);

        // 为新用户创建默认的任务类型
        taskTypeService.createDefaultTaskTypesForUser(user.getId());

        // 返回注册成功状态
        return R.success("用户注册成功，并赋予USER角色");
    }
}
