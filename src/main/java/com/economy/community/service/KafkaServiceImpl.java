package com.economy.community.service;

import com.economy.community.dto.UpdatePostUserNickName;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
    @KafkaListener(id = "community-nickname", topics = "updateusernickname")
    public void consume(UpdatePostUserNickName updatePostUserNickName) {
        System.out.println("Consumer IsBilling : " + updatePostUserNickName);
        //실제 동작 로직
    }
}
