package com.boat.mpp.common.dto.account.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 阿里云短信服务账号
 *
 * @author boat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AliSmsAccount extends SmsAccount{
    /**
     * api相关
     */
    private String url;
    private String region;

    /**
     * 账号相关
     */
    private String secretId;
    private String secretKey;
    // private String smsSdkAppId;
    private String templateCode;
    private String signName;
}
