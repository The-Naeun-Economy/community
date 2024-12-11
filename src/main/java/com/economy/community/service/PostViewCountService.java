package com.economy.community.service;

public interface PostViewCountService {

    Long incrementPostViewCount(Long id);

    Long getPostViewCount(Long id);
}
