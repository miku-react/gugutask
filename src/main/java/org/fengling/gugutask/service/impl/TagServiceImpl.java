package org.fengling.gugutask.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fengling.gugutask.dao.TagMapper;
import org.fengling.gugutask.pojo.Tag;
import org.fengling.gugutask.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;

    // 可以添加额外的业务逻辑方法
    @Override
    public List<Tag> findTagsByUserId(Long userId) {
        return this.baseMapper.findTagsByUserId(userId);
    }

    @Override
    public void removeByUserId(Long userId) {
        tagMapper.deleteByUserId(userId);
    }
}
