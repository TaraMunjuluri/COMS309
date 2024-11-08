package onetomany.websockets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import onetomany.MenteeSurvey.Mentee;
import onetomany.MenteeSurvey.MenteeRepository;
import onetomany.MentorSurvey.Mentor;
import onetomany.MentorSurvey.MentorRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;
import onetomany.Matches.MatchedPairMentorMentee;
import onetomany.Matches.MatchedPairRepository;
import onetomany.Matches.MatchedPersonDTO;
import onetomany.services.MatchMentorMenteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ServerEndpoint(value = "/matches-websocket/{user_id}", configurator = WebSocketConfig.WebSocketConfigurator.class)
public class MatchController {

    private static SimpMessagingTemplate template;
    private static UserRepository users_repo;
    private static MatchedPairRepository matchedPairRepository;
    private static MentorRepository mentorRepository;
    private static MenteeRepository menteeRepository;
    private static MatchMentorMenteeService matchMentorMenteeService;

    private final ConcurrentHashMap<Long, List<Session>> userSessions = new ConcurrentHashMap<>();

    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        MatchController.template = template;
    }

    @Autowired
    public void setUsersRepo(UserRepository repo) {
        MatchController.users_repo = repo;
    }

    @Autowired
    public void setMatchedPairRepository(MatchedPairRepository repo) {
        MatchController.matchedPairRepository = repo;
    }

    @Autowired
    public void setMentorRepository(MentorRepository repo) {
        MatchController.mentorRepository = repo;
    }

    @Autowired
    public void setMenteeRepository(MenteeRepository repo) {
        MatchController.menteeRepository = repo;
    }

    @Autowired
    public void setMatchMentorMenteeService(MatchMentorMenteeService service) {
        MatchController.matchMentorMenteeService = service;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("user_id") Long userId) {
        System.out.println("[Match WS]: New connection from user: " + userId);
        userSessions.computeIfAbsent(userId, k -> new ArrayList<>()).add(session);
        System.out.println("[Match WS]: Added session for user: " + userId + ", Total sessions: " +
                userSessions.get(userId).size());

        // Send initial matches when connected
        try {
            List<MatchedPersonDTO> matches = getMatchesForUser(userId);
            sendMatchesToSession(session, matches);
        } catch (Exception e) {
            System.out.println("[Match WS]: Error sending initial matches: " + e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("user_id") Long userId) {
        System.out.println("[Match WS] Received message: " + message + " from user: " + userId);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(message);

            if ("REQUEST_MATCHES".equals(node.get("type").asText())) {
                System.out.println("[Match WS] Processing REQUEST_MATCHES");
                List<MatchedPersonDTO> matches = getMatchesForUser(userId);
                System.out.println("[Match WS] Found " + matches.size() + " matches");
                sendMatchesToSession(session, matches);
            }
        } catch (Exception e) {
            System.out.println("[Match WS] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendMatchesToSession(Session session, List<MatchedPersonDTO> matches) throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "MATCHES_RESPONSE");
        response.put("timestamp", new Date().toString());
        response.put("matches", matches);

        String jsonResponse = new ObjectMapper().writeValueAsString(response);
        System.out.println("[Match WS] Sending response: " + jsonResponse);
        session.getBasicRemote().sendText(jsonResponse);
    }

    @OnClose
    public void onClose(Session session, @PathParam("user_id") Long userId) {
        List<Session> sessions = userSessions.get(userId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                userSessions.remove(userId);
            }
            System.out.println("[Match WS]: Closed session for user: " + userId);
        }
    }

    private List<MatchedPersonDTO> getMatchesForUser(Long userId) {
        System.out.println("[Match WS]: Getting matches for user: " + userId);
        User user = users_repo.findById(userId).orElse(null);
        if (user == null) {
            System.out.println("[Match WS]: User not found: " + userId);
            return new ArrayList<>();
        }
        return getMatchedPersons(user);
    }

    private List<MatchedPersonDTO> getMatchedPersons(User user) {
        List<MatchedPersonDTO> matchedPersonDTOList = new ArrayList<>();

        // Get matches where user is mentor
        List<MatchedPairMentorMentee> mentorMatches = matchedPairRepository.findByMentor(user);
        for (MatchedPairMentorMentee match : mentorMatches) {
            User menteeUser = match.getMentee();
            Mentee mentee = menteeRepository.findByUser(menteeUser);
            if (mentee != null) {
                matchedPersonDTOList.add(new MatchedPersonDTO(
                        menteeUser.getUsername(),
                        mentee.getClassification().toString(),
                        mentee.getMajor()
                ));
            }
        }

        // Get matches where user is mentee
        List<MatchedPairMentorMentee> menteeMatches = matchedPairRepository.findByMentee(user);
        for (MatchedPairMentorMentee match : menteeMatches) {
            User mentorUser = match.getMentor();
            Mentor mentor = mentorRepository.findByUser(mentorUser);
            if (mentor != null) {
                matchedPersonDTOList.add(new MatchedPersonDTO(
                        mentorUser.getUsername(),
                        mentor.getClassification().toString(),
                        mentor.getMajor()
                ));
            }
        }

        System.out.println("[Match WS]: Found " + matchedPersonDTOList.size() + " matches for user: " + user.getId());
        return matchedPersonDTOList;
    }

    @Scheduled(fixedRate = 5000)
    public void checkAndBroadcastNewMatches() {
        System.out.println("[Match WS]: Checking for new matches...");
        List<MatchedPairMentorMentee> newMatches = matchMentorMenteeService.findNewMatches();

        if (!newMatches.isEmpty()) {
            System.out.println("[Match WS]: Found " + newMatches.size() + " new matches");
            for (MatchedPairMentorMentee match : newMatches) {
                notifyMatchParticipants(match);
            }
        }
    }

    private void notifyMatchParticipants(MatchedPairMentorMentee match) {
        try {
            // Notify mentor
            List<Session> mentorSessions = userSessions.get(match.getMentor().getId());
            if (mentorSessions != null) {
                for (Session session : mentorSessions) {
                    sendMatchesToSession(session, getMatchesForUser(match.getMentor().getId()));
                }
            }

            // Notify mentee
            List<Session> menteeSessions = userSessions.get(match.getMentee().getId());
            if (menteeSessions != null) {
                for (Session session : menteeSessions) {
                    sendMatchesToSession(session, getMatchesForUser(match.getMentee().getId()));
                }
            }
        } catch (Exception e) {
            System.out.println("[Match WS]: Error notifying match participants: " + e.getMessage());
        }
    }
}