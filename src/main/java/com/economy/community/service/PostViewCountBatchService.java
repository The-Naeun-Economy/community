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

        Set<String> keys = redisTemplate.keys(postCacheRepository.generateViewCacheKey(0L).replace("0", "*"));

        if (keys != null) {
            for (String key : keys) {
                Long postId = extractPostIdFromKey(key); // postId 추출
                Long redisViewCount = (Long) redisTemplate.opsForValue().get(key);
                if (redisViewCount != null && redisViewCount > 0) {
                    // DB에서 현재 조회수 가져오기
                    Long dbViewCount = postRepository.findViewCountById(postId);

                    // DB에 조회수 누적 반영
                    Long totalViewCount = dbViewCount + redisViewCount;
                    postRepository.updatePostViewCount(postId, totalViewCount);

                    // Redis에서 최신 값을 업데이트 (삭제하지 않고 누적)
                    redisTemplate.opsForValue().set(key, totalViewCount); // Redis에 새로 업데이트된 값 저장
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
