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
        // 키가 없을 경우 초기값 설정
        if (Boolean.FALSE.equals(redisTemplate.hasKey(cacheKey))) {
            redisTemplate.opsForValue().set(cacheKey, 0L);
        }
        redisTemplate.opsForValue().increment(cacheKey);
    }

    // 좋아요 수 감소
    public void decrementLikeCount(Long postId) {
        String cacheKey = generateLikeCacheKey(postId);

        if (Boolean.FALSE.equals(redisTemplate.hasKey(cacheKey))) {
            redisTemplate.opsForValue().set(cacheKey, 0L);
        }

        redisTemplate.opsForValue().decrement(cacheKey);
    }

    // 좋아요 캐시 키 생성
    public String generateViewCacheKey(Long postId) {
        return String.format("%s::%d::views", CACHE_KEY, postId);
    }

    // 조회수 증가
    public void incrementViewCount(Long postId) {
        String cacheKey = generateViewCacheKey(postId);
        // 키가 없을 경우 초기값 설정
        if (Boolean.FALSE.equals(redisTemplate.hasKey(cacheKey))) {
            redisTemplate.opsForValue().set(cacheKey, 0L);
        }
        redisTemplate.opsForValue().increment(cacheKey);
    }

    // 조회수 가져오기
    public Long getViewCount(Long postId) {
        String cacheKey = generateViewCacheKey(postId);
        Object cachedValue = redisTemplate.opsForValue().get(cacheKey);

        if (cachedValue != null) {
            try {
                // Object를 String으로 변환 후 Long으로 변환
                return Long.valueOf(cachedValue.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Redis에 저장된 조회수 데이터가 숫자가 아닙니다: " + cachedValue);
            }
        }

        return 0L; // 캐시가 없는 경우 기본값
    }
}
