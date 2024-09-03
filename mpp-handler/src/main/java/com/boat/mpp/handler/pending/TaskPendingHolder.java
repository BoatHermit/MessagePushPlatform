package com.boat.mpp.handler.pending;

import com.dtp.core.thread.DtpExecutor;
import com.boat.mpp.handler.config.HandlerThreadPoolConfig;
import com.boat.mpp.handler.utils.GroupIdMappingUtils;
import com.boat.mpp.support.utils.ThreadPoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;


/**
 * 存储每种消息类型与 TaskPending 的关系
 *
 * @author boat
 */
@Component
public class TaskPendingHolder {

    @Autowired
    private ThreadPoolUtils threadPoolUtils;

    private Map<String, ExecutorService> taskPendingHolder = new HashMap<>(32);

    /**
     * 获取得到所有的groupId
     */
    private static List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 给每个渠道，每种消息类型初始化一个线程池
     */
    @PostConstruct
    public void init() {
        // example ThreadPoolName:mpp.im.notice
        // 可以通过apollo配置：dynamic-tp-apollo-dtp.yml  动态修改线程池的信息
        for (String groupId : groupIds) {
            DtpExecutor executor = HandlerThreadPoolConfig.getExecutor(groupId);
            threadPoolUtils.register(executor);

            taskPendingHolder.put(groupId, executor);
        }
    }

    /**
     * 得到对应的线程池
     */
    public ExecutorService route(String groupId) {
        return taskPendingHolder.get(groupId);
    }
}
