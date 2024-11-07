package com.economy.community;

import java.util.List;

public abstract interface CommunityService {
    public List<Community> getCommunities();

    public Community getCommunityById(long id);
}
