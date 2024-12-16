package org.fengling.gugutask.controller;

import org.fengling.gugutask.util.CosOP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class FileController {
    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文件无效，请上传图片文件！");
        }

        try {
            String url = CosOP.uploadFile(file, "avatars");
            return ResponseEntity.ok().body(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败，请稍后再试！");
        }
    }
}
