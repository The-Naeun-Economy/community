package com.economy.community;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService service;

    @GetMapping
    public List<Community> getCommunities() {
        return service.getCommunities();
    }

    @GetMapping("/{id}")
    public Community getCommunity(@PathVariable Long id) {
        return service.getCommunityById(id);
    }

    @PostMapping
    public Community createCommunity(@RequestBody CommunityRequest request) {

    }

    @PutMapping("/{id}")
    public Community updateCommunity(@RequestBody CommunityRequest request, @PathVariable Long id) {

    }

    @DeleteMapping("/{id}")
    public void deleteCommunity(@PathVariable Long id) {

    }
}
