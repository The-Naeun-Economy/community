package com.economy.community.repository;

import com.economy.community.dto.PostResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCacheRepository implements CacheRepository {

    public static final String CACHE_KEY = "posts-cache";
    private final String cacheKey = CACHE_KEY;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String getCacheKey() {
        return cacheKey;
    }

    @Override
    public List<PostResponse> getCacheDate(String cacheKey) {
        return (List<PostResponse>) redisTemplate.opsForValue().get(cacheKey);
    }

    @Override
    public void saveCacheData(String cacheKey, Object data) {
        redisTemplate.opsForValue().set(cacheKey, data);
    }

    @Override
    public void deleteCacheData(String cacheKey) {
        redisTemplate.delete(cacheKey);
    }

    // 좋아요 캐시 키 생성
    public String generateLikeCacheKey(Long postId) {
        return String.format("%s::%d::likes", CACHE_KEY, postId);
    }

    // 좋아요 수 가져오기
    public Long getLikeCount(Long postId) {
        String cacheKey = generateLikeCacheKey(postId);
        Object cachedValue = redisTemplate.opsForValue().get(cacheKey);

        if (cachedValue != null) {
            try {
                // Object를 String으로 변환 후 Long으로 변환
                return Long.valueOf(cachedValue.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Redis에 저장된 좋아요 데이터가 숫자가 아닙니다: " + cachedValue);
            }
        }

        return 0L;
    }

    // 좋아요 수 증가
    public void incrementLikeCount(Long postId) {
        String cacheKey = generateLikeCacheKey(postId);
        redisTemplate.opsForValue().increment(cacheKey);
    }

    // 좋아요 수 감소
    public void decrementLikeCount(Long postId) {
        String cacheKey = generateLikeCacheKey(postId);
        redisTemplate.opsForValue().decrement(cacheKey);
    }
}
