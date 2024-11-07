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

    private final CommunityServiceImpl service;

    @GetMapping
    public List<Community> getCommunities() {
        return service.getCommunities();
    }

    @GetMapping("/{id}")
    public Community getCommunity(@PathVariable Long id) {
        return service.getCommunityById(id);
    }
}
