package com.adamdubiel.workshop.metrics.solutions.ex2;

import com.adamdubiel.workshop.metrics.domain.LunchPlace;
import com.adamdubiel.workshop.metrics.domain.LunchPlacesService;
import com.adamdubiel.workshop.metrics.domain.Voter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/places")
public class PlacesEndpoint {

    private final LunchPlacesService lunchPlacesService;

    private final Voter voter;

    @Autowired
    public PlacesEndpoint(LunchPlacesService lunchPlacesService, Voter voter, MetricRegistry metricRegistry) {
        this.lunchPlacesService = lunchPlacesService;
        this.voter = voter;
    }

    @RequestMapping(method = RequestMethod.GET)
    @Timed(name = "endpoint.list", absolute = true)
    public List<LunchPlace> list() {
        return lunchPlacesService.list();
    }

    @RequestMapping(path = "/{lunchPlaceName}/upvote", method = RequestMethod.GET)
    @Counted(name = "endpoint.upvote", absolute = true)
    public void upvote(@PathVariable String lunchPlaceName) {
        voter.castVote(new Voter.Vote(lunchPlaceName, 1, Voter.VoteType.UPVOTE));
    }

    @RequestMapping(path = "/{lunchPlaceName}/downvote", method = RequestMethod.GET)
    @Metered(name = "endpoint.downvote", absolute = true)
    public void downvote(@PathVariable String lunchPlaceName) {
        voter.castVote(new Voter.Vote(lunchPlaceName, 1, Voter.VoteType.DOWNVOTE));
    }
}
