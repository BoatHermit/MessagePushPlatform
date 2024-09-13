package com.boat.mpp.handler.handler.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSON;
import com.boat.mpp.common.constant.CommonConstant;
import com.boat.mpp.support.service.ConfigService;
import com.google.common.base.Throwables;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.common.dto.model.EmailContentModel;
import com.boat.mpp.common.enums.ChannelType;
import com.boat.mpp.handler.handler.BaseHandler;
import com.boat.mpp.support.domain.MessageTemplate;
import com.boat.mpp.support.utils.MppFileUtils;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * 邮件发送处理
 *
 * @author boat
 */
@Component
@Slf4j
public class EmailHandler extends BaseHandler{

    private static final String EMAIL_ACCOUNT_KEY = "emailAccount";
    @Value("${mpp.business.upload.crowd.path}")
    private String dataPath;
    private ConfigService configService;

    @Autowired
    public EmailHandler(ConfigService config) {
        channelCode = ChannelType.EMAIL.getCode();
        this.configService = config;
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccountConfig(taskInfo.getSendAccount());
        try {
            File file = StrUtil.isNotBlank(emailContentModel.getUrl()) ?
                    MppFileUtils.getRemoteUrl2File(dataPath, emailContentModel.getUrl()) : null;
            if (Objects.isNull(file)) {
                MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle()
                        , emailContentModel.getContent(), true);
            } else {
                MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle()
                        , emailContentModel.getContent(), true, file);
            }
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
        // String defaultConfig = "{\"host\":\"smtp.qq.com\",\"port\":465,\"user\":\"403686131@qq.com\",\"pass\":\"123123123\",\"from\":\"403686131@qq.com\",\"starttlsEnable\":\"true\",\"auth\":true,\"sslEnable\":true}";
        // MailAccount account = accountUtils.getAccountById(sendAccount, MailAccount.class);

        String config = configService.getProperty(EMAIL_ACCOUNT_KEY, CommonConstant.EMPTY_JSON_OBJECT);
        MailAccount account = JSON.parseObject(config, MailAccount.class);
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setAuth(account.isAuth()).setStarttlsEnable(account.isStarttlsEnable()).setSslEnable(account.isSslEnable()).setCustomProperty("mail.smtp.ssl.socketFactory", sf);
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
