package org.fengling.gugutask.controller;

import org.fengling.gugutask.pojo.Tag;
import org.fengling.gugutask.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    // 查询所有标签
    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.list();
    }

    // 查询单个标签
    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id, @RequestParam Long userId) {
        Tag tag = tagService.getById(id);
        return (tag != null && tag.getUserId().equals(userId)) ? tag : null;
    }

    // 创建标签
    @PostMapping
    public boolean createTag(@RequestBody Tag tag, @RequestParam Long userId) {
        tag.setUserId(userId);  // 设置userId到tag对象中
        return tagService.save(tag);
    }

    // 更新标签
    @PutMapping("/{id}")
    public boolean updateTag(@PathVariable Long id, @RequestBody Tag tag, @RequestParam Long userId) {
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
    public boolean deleteTag(@PathVariable Long id, @RequestParam Long userId) {
        Tag existingTag = tagService.getById(id);
        if (existingTag != null && existingTag.getUserId().equals(userId)) {
            return tagService.removeById(id);
        }
        return false;
    }

    // 按照userid查找tag
    @GetMapping("/user/{userId}")
    public List<Tag> getTagsByUserId(@PathVariable Long userId) {
        return tagService.findTagsByUserId(userId);
    }
}
