package com.economy.community.service;

import com.economy.community.domain.Notification;
import com.economy.community.repository.PostCacheRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final PostCacheRepository postCacheRepository;

    // 사용자별 알림 조회
    public List<Notification> getUserNotifications(Long userId) {
        return postCacheRepository.getNotifications(userId);
    }

    // 사용자별 알림 삭제
    public void clearUserNotifications(Long userId) {
        postCacheRepository.deleteNotifications(userId);
    }
}
