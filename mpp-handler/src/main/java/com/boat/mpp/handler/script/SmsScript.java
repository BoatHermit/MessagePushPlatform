package com.boat.mpp.handler.script;

import com.boat.mpp.handler.domain.sms.SmsParam;
import com.boat.mpp.support.domain.SmsRecord;

import java.util.List;

/**
 * 短信脚本 接口
 *
 * @author boat
 */
public interface SmsScript {

    /**
     * 发送短信
     *
     * @param smsParam
     * @return 渠道商发送接口返回值
     */
    List<SmsRecord> send(SmsParam smsParam);


    /**
     * 拉取回执
     *
     * @param id 渠道账号的ID
     * @return 渠道商回执接口返回值
     */
    List<SmsRecord> pull(Integer id);

}

