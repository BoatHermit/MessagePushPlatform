package com.boat.mpp.handler.deduplication.limit.impl;

import cn.hutool.core.util.IdUtil;
import com.boat.mpp.common.domain.TaskInfo;
import com.boat.mpp.handler.deduplication.DeduplicationParam;
import com.boat.mpp.handler.deduplication.service.impl.AbstractDeduplicationService;
import com.boat.mpp.support.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 滑动窗口去重器（内容去重采用基于 redis 中 zset 的滑动窗口去重，可以做到严格控制单位时间内的频次。）
 *
 * @author boat
 */
@Service(value = "SlideWindowLimitService")
public class SlideWindowLimitService extends AbstractLimitService {

    private static final String LIMIT_TAG = "SW_";
    private final RedisUtils redisUtils;
    private DefaultRedisScript<Long> redisScript;

    @Autowired
    public SlideWindowLimitService(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("limit.lua")));
    }


    /**
     * @param service  去重器对象
     * @param taskInfo
     * @param param    去重参数
     * @return 返回不符合条件的手机号码
     */
    @Override
    public Set<String> limitFilter(AbstractDeduplicationService service
            , TaskInfo taskInfo, DeduplicationParam param) {

        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        long nowTime = System.currentTimeMillis();
        for (String receiver : taskInfo.getReceiver()) {
            String key = LIMIT_TAG + deduplicationSingleKey(service, taskInfo, receiver);
            String scoreValue = String.valueOf(IdUtil.getSnowflake().nextId());
            String score = String.valueOf(nowTime);
            if (redisUtils.execLimitLua(redisScript, Collections.singletonList(key)
                    , String.valueOf(param.getDeduplicationTime() * 1000), score
                    , String.valueOf(param.getCountNum()), scoreValue)) {
                filterReceiver.add(receiver);
            }

        }
        return filterReceiver;
    }
}

