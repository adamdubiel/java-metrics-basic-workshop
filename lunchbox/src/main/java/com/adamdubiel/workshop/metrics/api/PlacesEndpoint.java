package com.adamdubiel.workshop.metrics.api;

import com.adamdubiel.workshop.metrics.domain.LunchPlace;
import com.adamdubiel.workshop.metrics.domain.LunchPlacesService;
import com.adamdubiel.workshop.metrics.domain.Voter;
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

    @Autowired
    public PlacesEndpoint(LunchPlacesService lunchPlacesService, Voter voter) {
        this.lunchPlacesService = lunchPlacesService;
        this.voter = voter;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<LunchPlace> list() {
        long startTime = System.currentTimeMillis();
        List<LunchPlace> places = lunchPlacesService.list();
        logger.info("Listing places took {}ms", System.currentTimeMillis() - startTime);

        return places;
    }

    // never ever use GET to modify resources - this here is only to simplify traffic generation
    @RequestMapping(path = "/{lunchPlaceName}/upvote", method = RequestMethod.GET)
    public void upvote(@PathVariable String lunchPlaceName) {
        voter.castVote(new Voter.Vote(lunchPlaceName, 1, Voter.VoteType.UPVOTE));
    }

    @RequestMapping(path = "/{lunchPlaceName}/downvote", method = RequestMethod.GET)
    public void downvote(@PathVariable String lunchPlaceName) {
        voter.castVote(new Voter.Vote(lunchPlaceName, 1, Voter.VoteType.DOWNVOTE));
    }
}
