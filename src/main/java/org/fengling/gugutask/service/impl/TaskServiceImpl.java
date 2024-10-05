package org.fengling.gugutask.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fengling.gugutask.dao.TaskMapper;
import org.fengling.gugutask.dto.TagD;
import org.fengling.gugutask.dto.TaskDetailsD;
import org.fengling.gugutask.dto.TaskTypeD;
import org.fengling.gugutask.pojo.Task;
import org.fengling.gugutask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    // 在这里重写Service层的业务逻辑
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<Task> getTasksByUserId(Long userId) {
        // 调用 TaskMapper 中的自定义查询方法
        return taskMapper.findTasksByUserId(userId);
    }

    @Override
    public void removeByUserId(Long userId) {
        taskMapper.deleteByUserId(userId);
    }


    @Override
    public List<TaskDetailsD> getTasksWithDetailsByUserIdAndType(Long userId, Long typeId) {
        List<Map<String, Object>> taskDataList = taskMapper.getTasksWithDetailsByUserIdAndType(userId, typeId);

        // 创建日期和时间的格式化器
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // 映射 Map 到 TaskDetailsD
        List<TaskDetailsD> taskDetailsDList = taskDataList.stream().map(taskData -> {
            TaskDetailsD taskDetailsD = new TaskDetailsD();
            taskDetailsD.setTaskId((Long) taskData.get("id"));
            taskDetailsD.setTaskName((String) taskData.get("name"));
            taskDetailsD.setTaskDetail((String) taskData.get("detail"));
            taskDetailsD.setTaskPriority((String) taskData.get("priority"));

            // 将 java.sql.Date 和 java.sql.Time 转换为 String
            Date date1 = (Date) taskData.get("date1");
            Time date2 = (Time) taskData.get("date2");
            taskDetailsD.setDate1(date1 != null ? Date.valueOf(date1.toString()) : null);  // 或者使用 dateFormatter.format(date1.toLocalDate())
            taskDetailsD.setDate2(date2 != null ? Time.valueOf(date2.toString()) : null);  // 或者使用 timeFormatter.format(date2.toLocalTime())

            taskDetailsD.setTaskStatus((String) taskData.get("status"));

            // 将 Timestamp 转换为 LocalDateTime
            Timestamp createdAt = (Timestamp) taskData.get("created_at");
            Timestamp updatedAt = (Timestamp) taskData.get("updated_at");
            taskDetailsD.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
            taskDetailsD.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

            // 处理任务类型
            TaskTypeD taskTypeD = new TaskTypeD();
            taskTypeD.setTaskTypeId((Long) taskData.get("type_id"));
            taskTypeD.setTypeName((String) taskData.get("type_name"));
            taskTypeD.setCreatedAt(((Timestamp) taskData.get("type_created_at")).toLocalDateTime());
            taskTypeD.setUpdatedAt(((Timestamp) taskData.get("type_updated_at")).toLocalDateTime());
            taskDetailsD.setTaskType(taskTypeD);

            // 处理标签，将 Timestamp 转换为 LocalDateTime
            List<TagD> tagDList = taskMapper.findTagsByTaskId((Long) taskData.get("id")).stream().map(tagData -> {
                TagD tagD = new TagD();
                tagD.setId((Long) tagData.get("id"));
                tagD.setTagName((String) tagData.get("tag_name"));
                return tagD;
            }).collect(Collectors.toList());
            taskDetailsD.setTags(tagDList);

            return taskDetailsD;
        }).collect(Collectors.toList());

        return taskDetailsDList;
    }
}

