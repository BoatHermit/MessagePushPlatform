package com.boat.mpp.handler.script.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.boat.mpp.common.dto.account.sms.AliSmsAccount;
import com.boat.mpp.common.dto.account.sms.TencentSmsAccount;
import com.boat.mpp.common.enums.SmsStatus;
import com.boat.mpp.handler.domain.sms.SmsParam;
import com.boat.mpp.handler.script.SmsScript;
import com.boat.mpp.support.domain.SmsRecord;
import com.boat.mpp.support.utils.AccountUtils;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.*;
import com.google.common.base.Throwables;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusRequest;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusResponse;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component("AliSmsScript")
@Builder
public class AliSmsScript implements SmsScript {

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public List<SmsRecord> send(SmsParam smsParam) {
        try {
            AliSmsAccount AliSmsAccount = accountUtils
                    .getAccountById(smsParam.getSendAccountId(), AliSmsAccount.class);
            AsyncClient client = init(AliSmsAccount);
            SendSmsRequest request = assembleSendReq(smsParam, AliSmsAccount);
            CompletableFuture<SendSmsResponse> response = client.sendSms(request);
            SendSmsResponse resp = response.get();
            return assembleSendSmsRecord(smsParam, resp, AliSmsAccount);
        } catch (Exception e) {
            log.error("AliSmsScript#send fail:{}, params:{}", e.getMessage(), JSON.toJSONString(smsParam));
            return null;
        }
    }

    @Override
    public List<SmsRecord> pull(Integer id) {
        //TODO
        return Collections.emptyList();
    }

    private AsyncClient init(AliSmsAccount account) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(account.getSecretId())
                .accessKeySecret(account.getSecretKey())
                .build());

        return AsyncClient.builder()
                .region(account.getRegion())
                .credentialsProvider(provider)
                .overrideConfiguration(ClientOverrideConfiguration.create()
                        .setEndpointOverride("dysmsapi.aliyuncs.com"))
                .build();
    }

    private SendSmsRequest assembleSendReq(SmsParam smsParam, AliSmsAccount account) {


        return SendSmsRequest.builder()
                .signName(account.getSignName())
                .templateCode(account.getTemplateCode())
                .phoneNumbers(String.join(",", smsParam.getPhones()))
                .templateParam(smsParam.getContent())
                .build();
    }

    private List<SmsRecord> assembleSendSmsRecord(SmsParam smsParam, SendSmsResponse response, AliSmsAccount account) {
        if (response == null || response.getBody() == null) {
            return null;
        }

        List<SmsRecord> smsRecordList = new ArrayList<>();
        SmsRecord smsRecord = SmsRecord.builder()
                .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                .messageTemplateId(smsParam.getMessageTemplateId())
                .phone(Long.valueOf(smsParam.getPhones().iterator().next()))
                .supplierId(account.getSupplierId())
                .supplierName(account.getSupplierName())
                .msgContent(smsParam.getContent())
                .seriesId(response.getBody().getBizId())
                .status(SmsStatus.SEND_SUCCESS.getCode())
                .reportContent(response.getBody().getMessage())
                .created(Math.toIntExact(DateUtil.currentSeconds()))
                .updated(Math.toIntExact(DateUtil.currentSeconds()))
                .build();

        smsRecordList.add(smsRecord);
        return smsRecordList;
    }
}
