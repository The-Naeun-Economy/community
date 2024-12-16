package com.economy.community.service;

import com.economy.community.repository.PostCacheRepository;
import com.economy.community.repository.PostRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostViewCountBatchService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final PostRepository postRepository;
    private final PostCacheRepository postCacheRepository;

    @Transactional
    @Scheduled(fixedRate = 60000) // 매 1분마다 실행
    public void syncViewCountsToDatabase() {
        System.out.println("PostViewCountBatchService 돌아가는 중임...");

        Set<String> keys = redisTemplate.keys("posts-cache::*::views");
        if (keys != null) {
            for (String key : keys) {
                Long postId = extractPostIdFromKey(key); // postId 추출
                Integer increment = (Integer) redisTemplate.opsForValue().get(key);
                if (increment != null && increment > 0) {
                    // 데이터베이스에 저장
                    postRepository.updatePostViewCount(postId, increment);
                    // Redis에서 초기화
                    redisTemplate.opsForValue().set(postCacheRepository.generateViewCacheKey(postId), 0L);
                }
            }
        }
    }

    // 키에서 postId를 추출하는 메서드
    private Long extractPostIdFromKey(String key) {
        // posts-cache::[postId]::views -> postId 추출
        String[] parts = key.split("::");
        return Long.parseLong(parts[1]);
    }
}
