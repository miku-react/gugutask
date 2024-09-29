package org.fengling.gugutask.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fengling.gugutask.pojo.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {
    // 可以在这里添加额外的业务逻辑方法
    List<Tag> findTagsByUserId(Long userId);

    // 根据 userId 删除 tags 中的记录
    void removeByUserId(Long userId);
}
