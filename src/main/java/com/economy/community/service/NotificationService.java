package com.economy.community.service;

import com.economy.community.domain.Notification;
import com.economy.community.repository.PostCacheRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final PostCacheRepository postCacheRepository;

    public void waitForUserNotifications(Long userId, DeferredResult<List<Notification>> deferredResult) {
        // 알림 데이터 확인
        List<Notification> notifications = postCacheRepository.getNotifications(userId);

        if (!notifications.isEmpty()) {
            deferredResult.setResult(notifications); // 알림 데이터가 있으면 즉시 반환
        } else {
            // 타임아웃 후 빈 리스트 반환
            deferredResult.onTimeout(() -> deferredResult.setResult(Collections.emptyList()));
        }
    }

    // 사용자별 알림 삭제
    public void clearUserNotifications(Long userId) {
        postCacheRepository.deleteNotifications(userId);
    }
}
