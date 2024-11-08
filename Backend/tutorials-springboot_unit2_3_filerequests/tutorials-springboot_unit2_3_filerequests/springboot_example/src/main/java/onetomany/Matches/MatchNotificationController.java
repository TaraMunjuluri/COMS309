package onetomany.Matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MatchNotificationController {

    @Autowired
    private SimpMessagingTemplate template;
    
    @GetMapping("/matches/notifications")
    public String getMatchNotificationsPage() {
        return "matches/index"; // This will look for src/main/resources/static/matches/index.html
    }
    @GetMapping("/send-message")
    public ResponseEntity<String> sendTestMessage() {
        // Send a test message to the WebSocket topic
        template.convertAndSend("/topic/matches", "Test message from server to WebSocket");
        return ResponseEntity.ok("Test message sent to WebSocket topic");
    }
}