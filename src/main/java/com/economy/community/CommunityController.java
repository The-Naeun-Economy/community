package com.economy.community;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class CommunityController {

    @GetMapping
    public List<Community> getCommunities() {
        return CommunityService.getCommunities();
    }

    @GetMapping("/{community_id}")
    public String getCommunityById(@PathVariable Long communityId) {

    }
}
