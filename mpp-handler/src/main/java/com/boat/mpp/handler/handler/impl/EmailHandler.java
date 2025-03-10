package com.boat.mpp.handler.handler.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.boat.mpp.handler.enums.RateLimitStrategy;
import com.boat.mpp.handler.flowcontrol.FlowControlParam;
import com.boat.mpp.support.utils.AccountUtils;
import com.google.common.base.Throwables;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.common.dto.model.EmailContentModel;
import com.boat.mpp.common.enums.ChannelType;
import com.boat.mpp.handler.handler.BaseHandler;
import com.boat.mpp.support.domain.MessageTemplate;
import com.boat.mpp.support.utils.MppFileUtils;
import com.google.common.util.concurrent.RateLimiter;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * 邮件发送处理
 *
 * @author boat
 */
@Component
@Slf4j
public class EmailHandler extends BaseHandler{

    @Autowired
    private AccountUtils accountUtils;

    @Value("${mpp.business.upload.crowd.path}")
    private String dataPath;

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();
        // 按照请求限流，默认单机 3 qps （具体数值配置在apollo动态调整)
        Double rateInitValue = Double.valueOf(3);
        flowControlParam = FlowControlParam.builder().rateInitValue(rateInitValue)
                .rateLimitStrategy(RateLimitStrategy.REQUEST_RATE_LIMIT)
                .rateLimiter(RateLimiter.create(rateInitValue)).build();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccountConfig(taskInfo.getSendAccount());
        try {
            List<File> files = StrUtil.isNotBlank(emailContentModel.getUrl()) ? MppFileUtils
                    .getRemoteUrl2File(dataPath, StrUtil.split(emailContentModel.getUrl(), StrUtil.COMMA)) : null;
            String result = CollUtil.isEmpty(files)
                    ? MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle()
                        , emailContentModel.getContent(), true) :
                    MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle()
                            , emailContentModel.getContent(), true, files.toArray(new File[files.size()]));
        } catch (Exception e) {
            log.error("EmailHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }
        return true;
    }

    /**
     * 获取账号信息和配置
     *
     * @return
     */
    private MailAccount getAccountConfig(Integer sendAccount) {

        MailAccount account = accountUtils.getAccountById(sendAccount, MailAccount.class);
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setAuth(account.isAuth()).setStarttlsEnable(account.isStarttlsEnable())
                    .setSslEnable(account.isSslEnable()).setCustomProperty("mail.smtp.ssl.socketFactory", sf);
            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (Exception e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {
        //TODO
    }
}
