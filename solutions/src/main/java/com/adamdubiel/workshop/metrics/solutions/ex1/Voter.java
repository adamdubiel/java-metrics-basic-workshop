package com.adamdubiel.workshop.metrics.solutions.ex1;

import com.adamdubiel.workshop.metrics.domain.LunchPlacesRepository;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import org.jctools.queues.MpscArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Voter {

    private static final Logger logger = LoggerFactory.getLogger(Voter.class);

    private final MpscArrayQueue<Vote> voteQueue;

    private final LunchPlacesRepository repository;

    private long lastRunDuration;

    @Autowired
    public Voter(LunchPlacesRepository repository, MetricRegistry metricRegistry) {
        this.repository = repository;
        this.voteQueue = new MpscArrayQueue<>(100);

        metricRegistry.register("background.voter.time", (Gauge<Long>) () -> lastRunDuration);
    }

    public void castVote(Vote vote) {
        voteQueue.add(vote);
    }

    @Scheduled(fixedDelayString = "${voteCounter.delay:10000}")
    public void countVotes() {
        logger.info("Start counting votes");
        long startTime = System.currentTimeMillis();
        voteQueue.drain((v) -> {
                    switch (v.type) {
                        case UPVOTE:
                            repository.upvote(v.placeName, v.amount);
                            break;
                        case DOWNVOTE:
                            repository.downvote(v.placeName, v.amount);
                            break;
                    }
                }

        );
        this.lastRunDuration = System.currentTimeMillis() - startTime;
        logger.info("Done counting votes");
    }

    public static class Vote {
        final String placeName;
        final int amount;
        final VoteType type;

        public Vote(String placeName, int amount, VoteType type) {
            this.placeName = placeName;
            this.amount = amount;
            this.type = type;
        }
    }

    public enum VoteType {UPVOTE, DOWNVOTE}

    ;
}
