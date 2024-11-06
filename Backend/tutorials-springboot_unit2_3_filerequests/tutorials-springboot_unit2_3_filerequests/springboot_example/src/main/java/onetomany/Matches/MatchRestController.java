package onetomany.websockets;

import onetomany.services.MatchMentorMenteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MatchRestController {

    @Autowired
    private MatchMentorMenteeService matchService;

    // HTTP GET endpoint to trigger matching
    @GetMapping("/match")
    public List<onetomany.matches.MatchedPairMentorMentee> getMatches() {
        return matchService.findMatches();
    }

}
