package onetomany.chatbox;


import com.fasterxml.jackson.databind.ObjectMapper;
import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import onetomany.chatbox.Message;
import java.time.LocalDateTime;
import java.util.Map;

import onetomany.BadWords.ProfanityFilterService;

@RestController
public class ChatWebSocket extends TextWebSocketHandler {


    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfanityFilterService profanityFilterService;

    private final ObjectMapper objectMapper = new ObjectMapper();

//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//
//        // Assuming the payload is in JSON format
//        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//
//        if (profanityFilterService.containsProfanity(chatMessage.getContent())) {
//            session.sendMessage(new TextMessage("Profanity is not allowed!"));
//            return;
//        }
//
//        // Fetch the user who sent the message
//        User user = userRepository.findByUsername(chatMessage.getUsername());
//
//        if (user != null) {
//            Message newMessage = new Message();
//            newMessage.setContent(chatMessage.getContent());
//            newMessage.setTimestamp(LocalDateTime.now());
//            newMessage.setUser(user);
//
//
//            messageRepository.save(newMessage);
//        }
//
//    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        // Assuming the payload is in JSON format
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

        // Check for profanity before processing the message
        if (profanityFilterService.containsProfanity(chatMessage.getContent())) {
            // Create a new message specifically for profanity warning
            TextMessage warningMessage = new TextMessage(
                    objectMapper.writeValueAsString(
                            Map.of("type", "error", "message", "Profanity is not allowed!")
                    )
            );

            // Send warning message back to the specific session
            session.sendMessage(warningMessage);

            // Early return to prevent message processing
            return;
        }

        // Fetch the user who sent the message
        User user = userRepository.findByUsername(chatMessage.getUsername());

        if (user != null) {
            Message newMessage = new Message();
            newMessage.setContent(chatMessage.getContent());
            newMessage.setTimestamp(LocalDateTime.now());
            newMessage.setUser(user);

            messageRepository.save(newMessage);
        }

        // Additional logic for broadcasting the message to other users
        // (implementation depends on your specific chat broadcasting mechanism)
    }

    // Create a class to map incoming chat messages
    private static class ChatMessage {
        private String username;
        private String content;


        public String getUsername() {
            return username;
        }


        public void setUsername(String username) {
            this.username = username;
        }


        public String getContent() {
            return content;
        }


        public void setContent(String content) {
            this.content = content;
        }
    }
}

