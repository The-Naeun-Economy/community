package com.economy.community.controller;

import com.economy.community.domain.Notification;
import com.economy.community.service.NotificationService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public DeferredResult<List<Notification>> getNotifications(@PathVariable Long userId) {
        // 5초 타임아웃 설정
        DeferredResult<List<Notification>> deferredResult = new DeferredResult<>(5000L, Collections.emptyList());

        notificationService.waitForUserNotifications(userId, deferredResult);

        return deferredResult;
    }

    @DeleteMapping("/{userId}")
    public void clearNotifications(@PathVariable Long userId) {
        notificationService.clearUserNotifications(userId);
    }
}