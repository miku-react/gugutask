package org.fengling.gugutask.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fengling.gugutask.dao.TagMapper;
import org.fengling.gugutask.pojo.Tag;
import org.fengling.gugutask.service.TagService;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    // 可以添加额外的业务逻辑方法
}
