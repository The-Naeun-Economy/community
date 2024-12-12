package com.economy.community.service;


public interface KafkaService {

    void incrementCommentsCount(Long postId);

    void decrementCommentsCount(Long postId);
}
