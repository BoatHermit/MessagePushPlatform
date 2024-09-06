package com.boat.mpp.handler.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.boat.mpp.common.domain.AnchorInfo;
import com.boat.mpp.common.domain.LogParam;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.common.enums.AnchorState;
import com.boat.mpp.handler.pending.Task;
import com.boat.mpp.handler.pending.TaskPendingHolder;
import com.boat.mpp.handler.service.ConsumeService;
import com.boat.mpp.handler.utils.GroupIdMappingUtils;
import com.boat.mpp.support.domain.MessageTemplate;
import com.boat.mpp.support.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumeServiceImpl implements ConsumeService {
    private static final String LOG_BIZ_TYPE = "Receiver#consumer";
    private static final String LOG_BIZ_RECALL_TYPE = "Receiver#recall";

    private final ApplicationContext context;


    private final TaskPendingHolder taskPendingHolder;
    private final LogUtils logUtils;

    @Autowired
    public ConsumeServiceImpl(LogUtils logUtils, ApplicationContext context, TaskPendingHolder taskPendingHolder) {
        this.logUtils = logUtils;
        this.context = context;
        this.taskPendingHolder = taskPendingHolder;
    }

    @Override
    public void consume2Send(List<TaskInfo> taskInfoLists) {
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoLists.iterator()));
        for (TaskInfo taskInfo : taskInfoLists) {
            Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
            logUtils.print(LogParam.builder().bizType(LOG_BIZ_TYPE).object(taskInfo).build()
                    , AnchorInfo.builder().ids(taskInfo.getReceiver())
                            .businessId(taskInfo.getBusinessId()).state(AnchorState.RECEIVE.getCode()).build());
            taskPendingHolder.route(topicGroupId).execute(task);
        }
    }

    @Override
    public void consume2recall(MessageTemplate messageTemplate) {
        logUtils.print(LogParam.builder().bizType(LOG_BIZ_RECALL_TYPE).object(messageTemplate).build());
        // TODO
    }
}
