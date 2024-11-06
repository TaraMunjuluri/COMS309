package onetomany.websockets;

import onetomany.services.MatchMentorMenteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MatchController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MatchMentorMenteeService matchService;

    @Scheduled(fixedRate = 5000) // Runs every 5 seconds; adjust as needed
    public void notifyMatches() {
        List<onetomany.matches.MatchedPairMentorMentee> matches = matchService.findMatches();
        if (!matches.isEmpty()) {
            template.convertAndSend("/topic/matches", matches);
        }
    }
}
