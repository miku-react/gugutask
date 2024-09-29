package org.fengling.gugutask.controller;

import org.fengling.gugutask.dto.TagD;
import org.fengling.gugutask.pojo.Tag;
import org.fengling.gugutask.security.jwt.JwtUtil;
import org.fengling.gugutask.service.TagService;
import org.fengling.gugutask.util.R;
import org.fengling.gugutask.util.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public R<TagD> getTagById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        Tag tag = tagService.getById(id);
        if (tag != null && tag.getUserId().equals(userId)) {
            TagD tagD = new TagD(tag.getTagName(), tag.getCreatedAt(), tag.getUpdatedAt());
            return R.success(tagD);
        } else {
            return R.forbidden("标签不属于该用户或不存在");
        }
    }

    // 创建标签
    @PostMapping
    public R<String> createTag(@RequestBody Tag tag, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        tag.setUserId(userId);  // 设置userId到tag对象中
        tag.setId(snowflakeIdGenerator.generateId());

        if (tagService.save(tag)) {
            return R.success("标签创建成功");
        } else {
            return R.error("标签创建失败");
        }
    }

    // 更新标签
    @PutMapping("/{id}")
    public R<String> updateTag(@PathVariable Long id, @RequestBody Tag tag, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        Tag existingTag = tagService.getById(id);
        if (existingTag != null && existingTag.getUserId().equals(userId)) {
            tag.setId(id);
            tag.setUserId(userId);  // 确保userId一致

            if (tagService.updateById(tag)) {
                return R.success("标签更新成功");
            } else {
                return R.error("标签更新失败");
            }
        } else {
            return R.forbidden("标签不属于该用户或不存在");
        }
    }

    // 删除标签
    @DeleteMapping("/{id}")
    public R<String> deleteTag(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        Tag existingTag = tagService.getById(id);
        if (existingTag != null && existingTag.getUserId().equals(userId)) {
            if (tagService.removeById(id)) {
                return R.success("标签删除成功");
            } else {
                return R.error("标签删除失败");
            }
        } else {
            return R.forbidden("标签不属于该用户或不存在");
        }
    }

    // 按照userId查找tag（使用JWT token获取userId）
    @GetMapping("/user")
    public R<List<TagD>> getTagsByUserId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // 提取JWT token
        Long userId = jwtUtil.extractUserId(token);  // 从JWT中提取userId

        List<Tag> tags = tagService.findTagsByUserId(userId);
        if (tags != null && !tags.isEmpty()) {
            List<TagD> tagDList = tags.stream()
                    .map(tag -> new TagD(tag.getTagName(), tag.getCreatedAt(), tag.getUpdatedAt()))
                    .collect(Collectors.toList());
            return R.success(tagDList);
        } else {
            return R.notFound("没有找到该用户的标签");
        }
    }
}
