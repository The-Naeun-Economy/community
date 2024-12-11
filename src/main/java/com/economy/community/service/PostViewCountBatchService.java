package com.economy.community.service;

import com.economy.community.repository.PostRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostViewCountBatchService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final PostRepository postRepository;
    private static final String VIEW_COUNT_KEY_PREFIX = "post:viewCount:";

    @Scheduled(fixedRate = 60000) // 매 1분마다 실행
    public void syncViewCountsToDatabase() {
        Set<String> keys = redisTemplate.keys(VIEW_COUNT_KEY_PREFIX + "*");
        if (keys != null) {
            for (String key : keys) {
                Long id = Long.valueOf(key.replace(VIEW_COUNT_KEY_PREFIX, ""));
                Integer increment = (Integer) redisTemplate.opsForValue().get(key);
                if (increment != null) {
                    // 데이터베이스에 저장
                    postRepository.updatePostViewCount(id, increment);
                    // Redis에서 초기화
                    redisTemplate.delete(key);
                }
            }
        }
    }
}
