package com.economy.community.event;

import com.economy.community.dto.PostResponse;
import com.economy.community.repository.CacheRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCacheListener {
    private final CacheRepository<List<PostResponse>> cacheRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateCacheWithNewSection(PostCacheUpdate event) {
        String cacheKey = String.format("%s::%d", cacheRepository.getCacheKey(),
                event.getPostId());

        try {
            // 캐싱된 데이터를 가져온다.
            List<PostResponse> cachingData = cacheRepository.getCacheDate(cacheKey);

            // 캐싱된 값이 존재할 경우
            if (cachingData != null && !cachingData.isEmpty()) {
                cachingData.add(PostResponse.from(event.getPost()));
                cacheRepository.saveCacheData(cacheKey, cachingData);
            }
        } catch (Exception ex) { // TODO 커스텀 예외 생성한다.
            // 캐싱되어 있는 데이터를 삭제한다.
            cacheRepository.deleteCacheData(cacheKey);
            log.error("캐싱 데이터 갱신 중 오류가 발생했습니다.", ex);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteCacheHandle(PostCacheDelete event) {
        String cacheKey = String.format("%s::%d", cacheRepository.getCacheKey(),
                event.getPostId());
        cacheRepository.deleteCacheData(cacheKey);
    }
}
