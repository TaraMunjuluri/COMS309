//package onetomany.chatbox;
//
//import java.io.IOException;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Map;
//
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
////import jakarta.websocket.OnClose;
////import jakarta.websocket.OnError;
////import jakarta.websocket.OnMessage;
////import jakarta.websocket.OnOpen;
////import jakarta.websocket.Session;
////import jakarta.websocket.server.PathParam;
////import jakarta.websocket.server.ServerEndpoint;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//
//import java.util.concurrent.ConcurrentHashMap;
//
//@Controller
//@ServerEndpoint(value = "/chat/{username}")
//public class ChatSocket {
//
//    private static MessageRepository msgRepo;
//
//    @Autowired
//    public void setMessageRepository(MessageRepository repo) {
//        msgRepo = repo;
//    }
//
//    private static Map<Session, String> sessionUsernameMap = new ConcurrentHashMap<>();
//    private static Map<String, Session> usernameSessionMap = new ConcurrentHashMap<>();
//    private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);
//
//    @OnOpen
//    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
//        logger.info("Entered into Open");
//        sessionUsernameMap.put(session, username);
//        usernameSessionMap.put(username, session);
//
//        // Send chat history to the newly connected user
//        sendMessageToParticularUser(username, getChatHistory());
//
//        // Broadcast that a new user joined
//        broadcast("User:" + username + " has Joined the Chat");
//    }
//
//    @OnMessage
//    public void onMessage(Session session, String message) throws IOException {
//        String username = sessionUsernameMap.get(session);
//
//        // Typing notification
//        if (message.equals("TYPING_START")) {
//            broadcastTypingStatus(username, true);
//        } else if (message.equals("TYPING_STOP")) {
//            broadcastTypingStatus(username, false);
//        } else {
//            // Handle regular messages
//            logger.info("Entered into Message: Got Message:" + message);
//            if (message.startsWith("@")) {
//                String destUsername = message.split(" ")[0].substring(1);
//                sendMessageToParticularUser(destUsername, "[DM] " + username + ": " + message);
//                sendMessageToParticularUser(username, "[DM] " + username + ": " + message);
//            } else {
//                broadcast(username + ": " + message);
//            }
//            msgRepo.save(new Message(username, message));
//        }
//    }
//
//    @OnClose
//    public void onClose(Session session) throws IOException {
//        String username = sessionUsernameMap.get(session);
//        sessionUsernameMap.remove(session);
//        usernameSessionMap.remove(username);
//
//        // Broadcast that the user disconnected
//        broadcast(username + " disconnected");
//    }
//
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        logger.info("Entered into Error");
//        throwable.printStackTrace();
//    }
//
//    private void sendMessageToParticularUser(String username, String message) {
//        try {
//            usernameSessionMap.get(username).getBasicRemote().sendText(message);
//        } catch (IOException e) {
//            logger.info("Exception: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private void broadcast(String message) {
//        sessionUsernameMap.forEach((session, username) -> {
//            try {
//                session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                logger.info("Exception: " + e.getMessage());
//                e.printStackTrace();
//            }
//        });
//    }
//
//    private void broadcastTypingStatus(String username, boolean isTyping) {
//        String message = isTyping ? username + " is typing..." : username + " stopped typing";
//        sessionUsernameMap.forEach((session, user) -> {
//            try {
//                session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                logger.info("Exception: " + e.getMessage());
//                e.printStackTrace();
//            }
//        });
//    }
//
//    private String getChatHistory() {
//        List<Message> messages = msgRepo.findAll();
//        StringBuilder sb = new StringBuilder();
//        if (messages != null && !messages.isEmpty()) {
//            for (Message message : messages) {
//                sb.append(message.getUserName()).append(": ").append(message.getContent()).append("\n");
//            }
//        }
//        return sb.toString();
//    }
//}
