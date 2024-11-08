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


@RestController
public class ChatWebSocket extends TextWebSocketHandler {


    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;


    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();


        // Assuming the payload is in JSON format
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);


        // Fetch the user who sent the message
        User user = userRepository.findByUsername(chatMessage.getUsername());


        if (user != null) {
            Message newMessage = new Message();
            newMessage.setContent(chatMessage.getContent());
            newMessage.setTimestamp(LocalDateTime.now());
            newMessage.setUser(user);


            messageRepository.save(newMessage);
        }


        // Send the message to other connected users or process it further
        // This part is handled by your existing chat broadcasting logic
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

