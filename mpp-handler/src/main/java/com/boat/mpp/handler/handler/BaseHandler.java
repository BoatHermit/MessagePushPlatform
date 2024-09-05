package com.boat.mpp.handler.handler;

import com.boat.mpp.common.domain.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 发送各个渠道的handler
 *
 * @author boat
 */
public abstract class BaseHandler implements Handler {

    @Autowired
    private HandlerHolder handlerHolder;

    /**
     * 标识渠道的Code
     * 子类初始化的时候指定
     */
    protected Integer channelCode;

    /**
     * 初始化渠道与Handler的映射关系
     */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }



    @Override
    public void doHandler(TaskInfo taskInfo) {
        if (handler(taskInfo)) {
            return;
        }
    }


    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     * @return
     */
    public abstract boolean handler(TaskInfo taskInfo);


}
