package com.boat.mpp.web.controller;


import com.boat.mpp.common.enums.ChannelType;
import com.boat.mpp.cron.handler.RefreshDingDingAccessTokenHandler;
import com.boat.mpp.web.annotation.MppAspect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author boat
 */

@MppAspect
@Api(tags = {"手动刷新token的接口"})
@RestController
public class RefreshTokenController {


    @Autowired
    private RefreshDingDingAccessTokenHandler refreshDingDingAccessTokenHandler;

    /**
     * 按照不同的渠道刷新对应的Token，channelType取值来源com.boat.mpp.common.enums.ChannelType
     *
     * @param channelType
     * @return
     */
    @ApiOperation(value = "手动刷新token", notes = "钉钉/个推 token刷新")
    @GetMapping("/refresh")
    public String refresh(Integer channelType) {
        if (ChannelType.DING_DING_WORK_NOTICE.getCode().equals(channelType)) {
            refreshDingDingAccessTokenHandler.execute();
        }
        return "刷新成功";
    }

}
