//package onetomany.websockets;
//import onetomany.MentorSurvey.MentorRepository;
//import onetomany.MenteeSurvey.MenteeRepository;
//import onetomany.MenteeSurvey.Mentee;
//import onetomany.MentorSurvey.Mentor;
//import onetomany.MenteeSurvey.Mentee;
//import onetomany.MentorSurvey.Mentor;
//import onetomany.Users.User;
//import onetomany.matches.MatchedPairMentorMentee;
//import onetomany.services.MatchMentorMenteeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Controller
//public class MatchController {
//
//    @Autowired
//    private SimpMessagingTemplate template;
//
//    @Autowired
//    private MatchMentorMenteeService matchMentorMenteeService; // Inject MatchMentorMenteeService
//
//    @Scheduled(fixedRate = 5000) // Runs every 5 seconds; adjust as needed
//    public void notifyMatches() {
//        System.out.println("Checking for new mentorship matches...");
//
//        // Call findNewMatches() from MatchMentorMenteeService
//        List<MatchedPairMentorMentee> newMatches = matchMentorMenteeService.findNewMatches();
//
//        // Check if there are any new matches
//        if (!newMatches.isEmpty()) {
//            // Send notification with the new matches
//            template.convertAndSend("/topic/matches", newMatches);
//            System.out.println("Mentorship match notification sent with new matches.");
//        } else {
//            System.out.println("No new mentorship matches found.");
//        }
//    }
//}
package onetomany.websockets;

import onetomany.MentorSurvey.MentorRepository;
import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.MenteeSurvey.Mentee;
import onetomany.MentorSurvey.Mentor;
import onetomany.matches.MatchedPairMentorMentee;
import onetomany.services.MatchMentorMenteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class MatchController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MatchMentorMenteeService matchMentorMenteeService;
    private Map<String, SimpMessageHeaderAccessor> userSessionMap = new ConcurrentHashMap<>();

    // ... (existing code)
    private Map<String, String> userSessions = new ConcurrentHashMap<>();

    public void addUserSession(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        userSessionMap.put(principal.getName(), headerAccessor);
    }

    public void removeUserSession(Principal principal) {
        userSessionMap.remove(principal.getName());
    }
    private void sendMessageToUser(SimpMessageHeaderAccessor headerAccessor, String message) {
        SimpMessageSendingOperations messagingTemplate = (SimpMessageSendingOperations) headerAccessor.getHeader("simpMessagingTemplate");
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSendToUser(headerAccessor.getUser().getName(), "/topic/matches", message);
        }
    }

    @Scheduled(fixedRate = 5000) // Runs every 5 seconds
    public void notifyMatches() {
        System.out.println("Checking for new mentorship matches...");

        // Retrieve new matches from the match service
        List<MatchedPairMentorMentee> newMatches = matchMentorMenteeService.findNewMatches();

        if (!newMatches.isEmpty()) {
            for (MatchedPairMentorMentee match : newMatches) {
                // Notify the mentor
                String mentorSessionId = userSessions.get(match.getMentor().getUsername());
                if (mentorSessionId != null) {
                    String mentorMessage = "You have been matched with mentee: " + match.getMentee().getUsername();
                    template.convertAndSendToUser(mentorSessionId, "/topic/matches", mentorMessage);
                }

                // Notify the mentee
                String menteeSessionId = userSessions.get(match.getMentee().getUsername());
                if (menteeSessionId != null) {
                    String menteeMessage = "You have been matched with mentor: " + match.getMentor().getUsername();
                    template.convertAndSendToUser(menteeSessionId, "/topic/matches", menteeMessage);
                }
            }
            System.out.println("Mentorship match notifications sent to " + newMatches.size() + " matched pairs.");
        } else {
            System.out.println("No new mentorship matches found.");
        }
    }

}