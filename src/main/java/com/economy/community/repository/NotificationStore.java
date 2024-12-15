package com.economy.community.repository;

import com.economy.community.domain.Notification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class NotificationStore {

    // 사용자 ID별 알림 리스트를 관리하는 맵
    private final Map<Long, List<Notification>> notifications = new ConcurrentHashMap<>();

    // 알림 추가
    public void addNotification(Long userId, String message, Long postId, String details) {
        Notification notification = new Notification(postId, message, details, LocalDateTime.now());

        // computeIfAbsent로 userId 키가 없을 경우 새로운 리스트 생성 후 추가
        notifications.computeIfAbsent(userId, k -> new ArrayList<>()).add(notification);
    }

    // 사용자 ID로 알림 조회
    public List<Notification> getNotifications(Long userId) {
        return notifications.getOrDefault(userId, Collections.emptyList());
    }

    // 사용자 ID로 알림 삭제
    public void deleteNotifications(Long userId) {
        notifications.remove(userId);
    }
}
