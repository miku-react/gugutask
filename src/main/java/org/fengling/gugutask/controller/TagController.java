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
    public Tag getTagById(@PathVariable Long id) {
        return tagService.getById(id);
    }

    // 创建标签
    @PostMapping
    public boolean createTag(@RequestBody Tag tag) {
        return tagService.save(tag);
    }

    // 更新标签
    @PutMapping("/{id}")
    public boolean updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        return tagService.updateById(tag);
    }

    // 删除标签
    @DeleteMapping("/{id}")
    public boolean deleteTag(@PathVariable Long id) {
        return tagService.removeById(id);
    }
}
