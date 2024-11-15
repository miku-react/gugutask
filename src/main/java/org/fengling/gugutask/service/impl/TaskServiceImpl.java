package org.fengling.gugutask.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    public List<TaskDetailsD> getTasksWithDetailsByUserId(Long userId) {
        List<Map<String, Object>> taskDataList = taskMapper.getTasksWithDetailsByUserId(userId);

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

    @Override
    public Page<TaskDetailsD> getTasksWithDetailsByUserIdAndTaskType(Long userId, Long taskType, int page, int size) {
        Page<Map<String, Object>> pageRequest = new Page<>(page, size);
        Page<Map<String, Object>> rawPageData = taskMapper.getTasksWithDetailsByUserIdAndTaskType(pageRequest, userId, taskType);

        // 使用 LinkedHashMap 按插入顺序存储任务，同时避免重复
        Map<Long, TaskDetailsD> taskMap = new LinkedHashMap<>();

        rawPageData.getRecords().forEach(record -> {
            Long taskId = (Long) record.get("id");

            // 如果任务已经存在于 map，则直接使用；否则创建一个新任务
            TaskDetailsD taskDetails = taskMap.computeIfAbsent(taskId, id -> {
                TaskDetailsD newTask = new TaskDetailsD();
                newTask.setTaskId(id);
                newTask.setTaskName((String) record.get("name"));
                newTask.setTaskDetail((String) record.get("detail"));
                newTask.setTaskPriority((String) record.get("priority"));
                newTask.setDate1((Date) record.get("date1"));
                newTask.setDate2((Time) record.get("date2"));
                newTask.setTaskStatus((String) record.get("status"));
                newTask.setCreatedAt(((Timestamp) record.get("created_at")).toLocalDateTime());
                newTask.setUpdatedAt(((Timestamp) record.get("updated_at")).toLocalDateTime());

                // 处理任务类型信息
                TaskTypeD taskTypeD = new TaskTypeD();
                taskTypeD.setTaskTypeId((Long) record.get("type_id"));
                taskTypeD.setTypeName((String) record.get("type_name"));
                taskTypeD.setCreatedAt(((Timestamp) record.get("type_created_at")).toLocalDateTime());
                taskTypeD.setUpdatedAt(((Timestamp) record.get("type_updated_at")).toLocalDateTime());
                newTask.setTaskType(taskTypeD);

                newTask.setTags(new ArrayList<>());  // 初始化标签列表
                return newTask;
            });

            // 添加标签信息
            if (record.get("tag_id") != null) {
                TagD tag = new TagD();
                tag.setId((Long) record.get("tag_id"));
                tag.setTagName((String) record.get("tag_name"));
                taskDetails.getTags().add(tag);
            }
        });

        // 将任务列表存入分页对象
        Page<TaskDetailsD> resultPage = new Page<>();
        resultPage.setCurrent(rawPageData.getCurrent());
        resultPage.setSize(rawPageData.getSize());
        resultPage.setTotal(rawPageData.getTotal());
        resultPage.setRecords(new ArrayList<>(taskMap.values()));  // 将 map 的 values 转为 list

        return resultPage;
    }
}

