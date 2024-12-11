package com.economy.community.service;

public interface PostViewCountService {
    
    int incrementPostViewCount(Long id);

    int getPostViewCount(Long id);
}
