package org.fengling.gugutask.controller;

import org.fengling.gugutask.pojo.Tag;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.TagService;
import org.fengling.gugutask.util.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    SnowflakeIdGenerator snowflakeIdGenerator;
    @Autowired
    private TagService tagService;
    @Autowired
    private JwtUtil jwtUtil;

    // 查询单个标签
    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // 提取JWT token
        Long userId = jwtUtil.extractUserId(token); // 从JWT中提取userId

        Tag tag = tagService.getById(id);
        return (tag != null && tag.getUserId().equals(userId)) ? tag : null;
    }

    // 创建标签
    @PostMapping
    public boolean createTag(@RequestBody Tag tag, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // 提取JWT token
        Long userId = jwtUtil.extractUserId(token); // 从JWT中提取userId

        tag.setUserId(userId);  // 设置userId到tag对象中
        tag.setId(snowflakeIdGenerator.generateId());
        return tagService.save(tag);
    }

    // 更新标签
    @PutMapping("/{id}")
    public boolean updateTag(@PathVariable Long id, @RequestBody Tag tag, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // 提取JWT token
        Long userId = jwtUtil.extractUserId(token); // 从JWT中提取userId

        Tag existingTag = tagService.getById(id);
        if (existingTag != null && existingTag.getUserId().equals(userId)) {
            tag.setId(id);
            tag.setUserId(userId);  // 确保userId一致
            return tagService.updateById(tag);
        }
        return false;
    }

    // 删除标签
    @DeleteMapping("/{id}")
    public boolean deleteTag(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // 提取JWT token
        Long userId = jwtUtil.extractUserId(token); // 从JWT中提取userId

        Tag existingTag = tagService.getById(id);
        if (existingTag != null && existingTag.getUserId().equals(userId)) {
            return tagService.removeById(id);
        }
        return false;
    }

    // 按照userId查找tag（使用JWT token获取userId）
    @GetMapping("/user")
    public List<Tag> getTagsByUserId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // 提取JWT token
        Long userId = jwtUtil.extractUserId(token); // 从JWT中提取userId

        return tagService.findTagsByUserId(userId);
    }
}
