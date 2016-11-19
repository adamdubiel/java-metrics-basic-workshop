package com.adamdubiel.workshop.metrics.infrastructure.refresh;

import com.adamdubiel.workshop.metrics.domain.LunchPlacesRepository;
import com.adamdubiel.workshop.metrics.infrastructure.leader.LeaderLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LunchPlaceRefresher {

    private static final Logger logger = LoggerFactory.getLogger(LunchPlaceRefresher.class);

    private final ExternalServiceClient externalServiceClient;

    private final LunchPlacesRepository repository;

    private final LeaderLock leaderLock;

    @Autowired
    public LunchPlaceRefresher(ExternalServiceClient externalServiceClient,
                               LunchPlacesRepository repository,
                               LeaderLock leaderLock) {
        this.externalServiceClient = externalServiceClient;
        this.repository = repository;
        this.leaderLock = leaderLock;
    }

    @Scheduled(fixedDelayString = "${refresher.delay:60000}")
    public void refreshLunchPlaces() {
        if (leaderLock.isLeader()) {
            logger.info("Start refreshing lunch places list");

            externalServiceClient.fetchPlaces().stream().forEach(repository::add);

            logger.info("Done refreshing lunch places list");
        }
    }
}
