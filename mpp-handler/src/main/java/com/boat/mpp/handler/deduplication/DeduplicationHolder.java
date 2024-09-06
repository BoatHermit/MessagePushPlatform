package com.boat.mpp.handler.deduplication;

import com.boat.mpp.handler.deduplication.build.DeduplicationBuilder;
import com.boat.mpp.handler.deduplication.service.DeduplicationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 去重服务工厂
 *
 * @author boat
 */
@Service
public class DeduplicationHolder {

    private final Map<Integer, DeduplicationBuilder> builderHolder = new HashMap<>(4);
    private final Map<Integer, DeduplicationService> serviceHolder = new HashMap<>(4);

    public DeduplicationBuilder selectBuilder(Integer key) {
        return builderHolder.get(key);
    }

    public DeduplicationService selectService(Integer key) {
        return serviceHolder.get(key);
    }

    public void putBuilder(Integer key, DeduplicationBuilder deduplicationBuilder) {
        builderHolder.put(key, deduplicationBuilder);
    }

    public void putService(Integer key, DeduplicationService service) {
        serviceHolder.put(key, service);
    }
}
