package onetomany.websockets;

import onetomany.matches.MatchedPair;
import onetomany.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MatchRestController {

    @Autowired
    private MatchService matchService;

    // HTTP GET endpoint to trigger matching
    @GetMapping("/match")
    public List<MatchedPair> getMatches() {
        return matchService.findMatches();
    }
}
