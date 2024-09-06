package com.boat.mpp.web.controller;


import cn.hutool.core.util.StrUtil;
import com.boat.mpp.common.constant.MppConstant;
import com.boat.mpp.support.domain.ChannelAccount;
import com.boat.mpp.web.annotation.MppAspect;
import com.boat.mpp.web.annotation.MppResult;
import com.boat.mpp.web.service.ChannelAccountService;
import com.boat.mpp.web.utils.Convert4Amis;
import com.boat.mpp.web.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 渠道账号管理接口
 */
@Api(tags = "渠道账号管理")
@Slf4j
@MppAspect
@MppResult
@RestController
@RequestMapping("/account")
public class ChannelAccountController {

    @Autowired
    private ChannelAccountService channelAccountService;

//    @Autowired
//    private LoginUtils loginUtils;

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    @ApiOperation("/保存数据")
    public ChannelAccount saveOrUpdate(@RequestBody ChannelAccount channelAccount) {
//        if (loginUtils.needLogin() && StrUtil.isBlank(channelAccount.getCreator())) {
//            throw new CommonException(RespStatusEnum.NO_LOGIN.getCode(), RespStatusEnum.NO_LOGIN.getMsg());
//        }
        channelAccount.setCreator(StrUtil.isBlank(channelAccount.getCreator()) ? MppConstant.DEFAULT_CREATOR : channelAccount.getCreator());

        return channelAccountService.save(channelAccount);
    }

    /**
     * 根据渠道标识查询渠道账号相关的信息
     */
    @GetMapping("/queryByChannelType")
    @ApiOperation("/根据渠道标识查询相关的记录")
    public List<CommonAmisVo> query(Integer channelType, String creator) {
//        if (loginUtils.needLogin() && StrUtil.isBlank(creator)) {
//            throw new CommonException(RespStatusEnum.NO_LOGIN.getCode(), RespStatusEnum.NO_LOGIN.getMsg());
//        }
        creator = StrUtil.isBlank(creator) ? MppConstant.DEFAULT_CREATOR : creator;

        List<ChannelAccount> channelAccounts = channelAccountService.queryByChannelType(channelType, creator);
        return Convert4Amis.getChannelAccountVo(channelAccounts, channelType);
    }

    /**
     * 所有的渠道账号信息
     */
    @GetMapping("/list")
    @ApiOperation("/渠道账号列表信息")
    public List<ChannelAccount> list(String creator) {
//        if (loginUtils.needLogin() && StrUtil.isBlank(creator)) {
//            throw new CommonException(RespStatusEnum.NO_LOGIN.getCode(), RespStatusEnum.NO_LOGIN.getMsg());
//
//        }
        creator = StrUtil.isBlank(creator) ? MppConstant.DEFAULT_CREATOR : creator;

        return channelAccountService.list(creator);
    }

    /**
     * 根据Id删除
     * id多个用逗号分隔开
     */
    @DeleteMapping("delete/{id}")
    @ApiOperation("/根据Ids删除")
    public void deleteByIds(@PathVariable("id") String id) {
        if (StrUtil.isNotBlank(id)) {
            List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(Long::valueOf).collect(Collectors.toList());
            channelAccountService.deleteByIds(idList);
        }
    }

}
