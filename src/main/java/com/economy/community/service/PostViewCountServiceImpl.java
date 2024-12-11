package com.economy.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostViewCountServiceImpl implements PostViewCountService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String VIEW_COUNT_KEY_PREFIX = "post:viewCount:";

    @Override
    public int incrementPostViewCount(Long id) {
        String redisKey = VIEW_COUNT_KEY_PREFIX + id;
        Long updatedCount = redisTemplate.opsForValue().increment(redisKey);
        return updatedCount != null ? updatedCount.intValue() : 0;
    }

    @Override
    public int getPostViewCount(Long id) {
        String redisKey = VIEW_COUNT_KEY_PREFIX + id;
        Integer count = (Integer) redisTemplate.opsForValue().get(redisKey);
        return count != null ? count : 0;
    }
}
