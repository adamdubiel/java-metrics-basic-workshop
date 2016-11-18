package com.adamdubiel.workshop.metrics.domain;

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

    @Autowired
    public Voter(LunchPlacesRepository repository) {
        this.repository = repository;
        this.voteQueue = new MpscArrayQueue<>(100);
    }

    public void castVote(Vote vote) {
        voteQueue.add(vote);
    }

    @Scheduled(fixedDelayString = "${voteCounter.delay:10000}")
    public void countVotes() {
        logger.info("Start counting votes");
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
