package com.boat.mpp.handler.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.handler.pending.Task;
import com.boat.mpp.handler.pending.TaskPendingHolder;
import com.boat.mpp.handler.service.ConsumeService;
import com.boat.mpp.handler.utils.GroupIdMappingUtils;
import com.boat.mpp.support.domain.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumeServiceImpl implements ConsumeService {
    private static final String LOG_BIZ_TYPE = "Receiver#consumer";
    private static final String LOG_BIZ_RECALL_TYPE = "Receiver#recall";
    @Autowired
    private ApplicationContext context;

    @Autowired
    private TaskPendingHolder taskPendingHolder;


    @Override
    public void consume2Send(List<TaskInfo> taskInfoLists) {
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoLists.iterator()));
        for (TaskInfo taskInfo : taskInfoLists) {
            Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
            taskPendingHolder.route(topicGroupId).execute(task);
            // TODO
        }
    }

    @Override
    public void consume2recall(MessageTemplate messageTemplate) {
        // TODO
    }
}
