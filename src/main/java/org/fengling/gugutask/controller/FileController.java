package org.fengling.gugutask.controller;

import lombok.RequiredArgsConstructor;
import org.fengling.gugutask.pojo.User;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.UserService;
import org.fengling.gugutask.util.CosOP;
import org.fengling.gugutask.util.R;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Transactional
    @PostMapping("/upload-avatar")
    public R<?> uploadAvatar(@RequestHeader("Authorization") String authHeader, @RequestParam("file") MultipartFile file) {
        try {
            String token = authHeader.substring(7);  // 提取JWT token
            String username = jwtUtil.extractUsername(token);  // 从JWT中提取用户名

            User user = userService.findByUsername(username);

            // 验证文件类型
            if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
                return R.error("文件无效，请上传图片文件！");
            }

            if (!file.getOriginalFilename().toLowerCase().matches(".*\\.(jpg|jpeg|png)$")) {
                return R.error("仅支持 JPG、JPEG 或 PNG 格式的图片！");
            }

            // 上传文件到对象存储
            String url = CosOP.uploadFile(file, "avatars");
            user.setAvatar(url);
            userService.updateById(user);
            return R.success(Map.of("url", url));

        } catch (IOException e) {
            return R.error("文件上传失败，请稍后再试！");
        }
    }
}
