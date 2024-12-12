package com.economy.community.service;

import com.economy.community.domain.Post;
import com.economy.community.dto.UpdatePostUserNickName;
import com.economy.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final PostRepository postRepository;

    @KafkaListener(id = "community-nickname", topics = "updateusernickname")
    public void consume(UpdatePostUserNickName updatePostUserNickName) {
        System.out.println("Consumer IsBilling : " + updatePostUserNickName);
        //실제 동작 로직
    }

    @Transactional
    public void incrementCommentsCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.incrementCommentsCount();
        postRepository.save(post);
    }

    @Transactional
    public void decrementCommentsCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.decrementCommentsCount();
        postRepository.save(post);
    }
}
