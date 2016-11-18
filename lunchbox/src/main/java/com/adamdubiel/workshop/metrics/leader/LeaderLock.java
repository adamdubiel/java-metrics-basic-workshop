package com.adamdubiel.workshop.metrics.leader;

import org.springframework.stereotype.Component;

@Component
public class LeaderLock {

    public boolean isLeader() {
        return true;
    }

}
