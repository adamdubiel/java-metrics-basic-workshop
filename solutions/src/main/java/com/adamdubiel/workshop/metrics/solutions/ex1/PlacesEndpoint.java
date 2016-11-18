package com.adamdubiel.workshop.metrics.solutions.ex1;

import com.adamdubiel.workshop.metrics.domain.LunchPlace;
import com.adamdubiel.workshop.metrics.domain.LunchPlacesService;
import com.adamdubiel.workshop.metrics.domain.Voter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/places")
public class PlacesEndpoint {

    private final Logger logger = LoggerFactory.getLogger(PlacesEndpoint.class);

    private final LunchPlacesService lunchPlacesService;

    private final Voter voter;

    private final MetricRegistry metricRegistry;

    @Autowired
    public PlacesEndpoint(LunchPlacesService lunchPlacesService, Voter voter, MetricRegistry metricRegistry) {
        this.lunchPlacesService = lunchPlacesService;
        this.voter = voter;
        this.metricRegistry = metricRegistry;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<LunchPlace> list() {
        try(Timer.Context c = metricRegistry.timer("endpoint.list").time()) {
            return lunchPlacesService.list();
        }
    }

    @RequestMapping(path = "/{lunchPlaceName}/upvote", method = RequestMethod.POST)
    public void upvote(@PathVariable String lunchPlaceName) {
        metricRegistry.counter("endpoint.upvote").inc();
        voter.castVote(new Voter.Vote(lunchPlaceName, 1, Voter.VoteType.UPVOTE));
    }

    @RequestMapping(path = "/{lunchPlaceName}/downvote", method = RequestMethod.POST)
    public void downvote(@PathVariable String lunchPlaceName) {
        metricRegistry.meter("endpoint.downvote").mark();
        voter.castVote(new Voter.Vote(lunchPlaceName, 1, Voter.VoteType.DOWNVOTE));
    }
}
