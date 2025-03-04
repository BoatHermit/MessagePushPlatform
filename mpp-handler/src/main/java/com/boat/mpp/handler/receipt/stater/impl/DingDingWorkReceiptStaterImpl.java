package com.boat.mpp.handler.receipt.stater.impl;

import com.boat.mpp.common.constant.CommonConstant;
import com.boat.mpp.common.enums.ChannelType;
import com.boat.mpp.handler.handler.impl.DingDingWorkNoticeHandler;
import com.boat.mpp.handler.receipt.stater.ReceiptMessageStater;
import com.boat.mpp.support.dao.ChannelAccountDao;
import com.boat.mpp.support.domain.ChannelAccount;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 拉取 钉钉工作消息的回执 内容 【未完成】
 * //TODO
 * @author boat
 */
public class DingDingWorkReceiptStaterImpl implements ReceiptMessageStater {

    // @Autowired
    private DingDingWorkNoticeHandler workNoticeHandler;

    // @Autowired
    private ChannelAccountDao channelAccountDao;

    @Override
    public void start() {
        List<ChannelAccount> accountList = channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.DING_DING_WORK_NOTICE.getCode());
        for (ChannelAccount channelAccount : accountList) {
            workNoticeHandler.pull(channelAccount.getId());
        }
    }
}
