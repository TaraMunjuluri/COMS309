package onetomany.Chatbox;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import onetomany.Chatbox.Message;
import onetomany.Chatbox.MessageRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@ServerEndpoint(value = "/chat/{username}")
public class ChatSocket {

    private static MessageRepository msgRepo;

    @Autowired
    public void setMessageRepository(MessageRepository repo) {
        msgRepo = repo;
    }

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

    @Autowired
    private UserRepository userRepository;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username)
            throws IOException {

        logger.info("Entered into Open");
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);
        sendMessageToPArticularUser(username, getChatHistory());
        String message = "User:" + username + " has Joined the Chat";
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("Entered into Message: Got Message: " + message);
        String username = sessionUsernameMap.get(session);

        // Retrieve the User object from the username
        User sender = userRepository.findByUsername(username);
        if (sender == null) {
            logger.warn("User not found: " + username);
            return;
        }

        // Direct message handling
        if (message.startsWith("@")) {
            String destUsername = message.split(" ")[0].substring(1);
            sendMessageToPArticularUser(destUsername, "[DM] " + username + ": " + message);
            sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);
        } else { // Broadcast to all users
            broadcast(username + ": " + message);
        }

        // Saving chat history to repository with User object
        msgRepo.save(new Message(sender, message));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        String message = username + " disconnected";
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }

    private void sendMessageToPArticularUser(String username, String message) {
        try {
            if (usernameSessionMap.containsKey(username)) {
                usernameSessionMap.get(username).getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("Exception: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private String getChatHistory() {
        List<Message> messages = msgRepo.findAll();
        StringBuilder sb = new StringBuilder();
        if (messages != null && !messages.isEmpty()) {
            for (Message message : messages) {
                String username = message.getSender() != null ? message.getSender().getUsername() : "Unknown User";
                sb.append(username + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }
}



//package onetomany.Chatbox;
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
//import onetomany.Chatbox.Message;
//import onetomany.Chatbox.MessageRepository;
//import onetomany.Users.User;
//import onetomany.Users.UserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//
//@Controller      // this is needed for this to be an endpoint to springboot
//@ServerEndpoint(value = "/chat/{username}")  // this is Websocket url
//public class ChatSocket {
//
//    // cannot autowire static directly (instead we do it by the below
//    // method
//    private static MessageRepository msgRepo;
//
//    /*
//     * Grabs the MessageRepository singleton from the Spring Application
//     * Context.  This works because of the @Controller annotation on this
//     * class and because the variable is declared as static.
//     * There are other ways to set this. However, this approach is
//     * easiest.
//     */
//    @Autowired
//    public void setMessageRepository(MessageRepository repo) {
//        msgRepo = repo;  // we are setting the static variable
//    }
//
//    // Store all socket session and their corresponding username.
//    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
//    private static Map<String, Session> usernameSessionMap = new Hashtable<>();
//
//    private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);
//
//    @Autowired
//    private UserRepository userRepository; // Inject UserRepository
//
//    @OnOpen
//    public void onOpen(Session session, @PathParam("username") String username)
//            throws IOException {
//
//        logger.info("Entered into Open");
//
//        // store connecting user information
//        sessionUsernameMap.put(session, username);
//        usernameSessionMap.put(username, session);
//
//        //Send chat history to the newly connected user
//        sendMessageToPArticularUser(username, getChatHistory());
//
//        // broadcast that new user joined
//        String message = "User:" + username + " has Joined the Chat";
//        broadcast(message);
//    }
//
//
////    @OnMessage
////    public void onMessage(Session session, String message) throws IOException {
////
////        // Handle new messages
////        logger.info("Entered into Message: Got Message:" + message);
////        String username = sessionUsernameMap.get(session);
////
////        // Direct message to a user using the format "@username <message>"
////        if (message.startsWith("@")) {
////            String destUsername = message.split(" ")[0].substring(1);
////
////            // send the message to the sender and receiver
////            sendMessageToPArticularUser(destUsername, "[DM] " + username + ": " + message);
////            sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);
////
////        }
////        else { // broadcast
////            broadcast(username + ": " + message);
////        }
////
////        // Saving chat history to repository
////        msgRepo.save(new Message(username, message));
////    }
//
//
//    @OnMessage
//    public void onMessage(Session session, String message) throws IOException {
//        logger.info("Entered into Message: Got Message: " + message);
//        String username = sessionUsernameMap.get(session);
//
//        // Retrieve the User object from the username
//        User sender = userRepository.findByUsername(username);
//
//        // Check if the sender exists
//        if (sender == null) {
//            logger.warn("User not found: " + username);
//            return; // Optionally, you can send an error message back to the client here
//        }
//
//        // Direct message to a user using the format "@username <message>"
//        if (message.startsWith("@")) {
//            String destUsername = message.split(" ")[0].substring(1);
//            sendMessageToPArticularUser(destUsername, "[DM] " + username + ": " + message);
//            sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);
//        } else { // Broadcast
//            broadcast(username + ": " + message);
//        }
//
//        // Saving chat history to repository with User object
//        msgRepo.save(new Message(sender, message));
//    }
//
//
//    @OnClose
//    public void onClose(Session session) throws IOException {
//        logger.info("Entered into Close");
//
//        // remove the user connection information
//        String username = sessionUsernameMap.get(session);
//        sessionUsernameMap.remove(session);
//        usernameSessionMap.remove(username);
//
//        // broadcase that the user disconnected
//        String message = username + " disconnected";
//        broadcast(message);
//    }
//
//
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        // Do error handling here
//        logger.info("Entered into Error");
//        throwable.printStackTrace();
//    }
//
//
//    private void sendMessageToPArticularUser(String username, String message) {
//        try {
//            usernameSessionMap.get(username).getBasicRemote().sendText(message);
//        }
//        catch (IOException e) {
//            logger.info("Exception: " + e.getMessage().toString());
//            e.printStackTrace();
//        }
//    }
//
//
//    private void broadcast(String message) {
//        sessionUsernameMap.forEach((session, username) -> {
//            try {
//                session.getBasicRemote().sendText(message);
//            }
//            catch (IOException e) {
//                logger.info("Exception: " + e.getMessage().toString());
//                e.printStackTrace();
//            }
//
//        });
//
//    }
//
//
//    // Gets the Chat history from the repository
//
//    private String getChatHistory() {
//        List<Message> messages = msgRepo.findAll();
//
//        // Convert the list to a string
//        StringBuilder sb = new StringBuilder();
//        if (messages != null && !messages.isEmpty()) {
//            for (Message message : messages) {
//                // Get the username from the sender User object
//                String username = message.getSender() != null ? message.getSender().getUsername() : "Unknown User";
//                sb.append(username + ": " + message.getContent() + "\n");
//            }
//        }
//        return sb.toString();
//    }
////    private String getChatHistory() {
////        List<Message> messages = msgRepo.findAll();
////
////        // convert the list to a string
////        StringBuilder sb = new StringBuilder();
////        if(messages != null && messages.size() != 0) {
////            for (Message message : messages) {
////                sb.append(message.getUserName() + ": " + message.getContent() + "\n");
////            }
////        }
////        return sb.toString();
////    }
//
//} // end of Class
//
//
//
//
//
////package onetomany.Chatbox;
////
////import java.io.IOException;
////import java.util.Hashtable;
////import java.util.List;
////import java.util.Map;
////
////import javax.websocket.*;
////import javax.websocket.server.PathParam;
////import javax.websocket.server.ServerEndpoint;
//////import jakarta.websocket.OnClose;
//////import jakarta.websocket.OnError;
//////import jakarta.websocket.OnMessage;
//////import jakarta.websocket.OnOpen;
//////import jakarta.websocket.Session;
//////import jakarta.websocket.server.PathParam;
//////import jakarta.websocket.server.ServerEndpoint;
////
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Controller;
////
////@Controller      // this is needed for this to be an endpoint to springboot
////@ServerEndpoint(value = "/chat/{username}")  // this is Websocket url
////public class ChatSocket {
////
////
////    @OnMessage
////    public void onMessage(Session session, String message) throws IOException {
////
////        // Handle new messages
////        logger.info("Entered into Message: Got Message:" + message);
////        String username = sessionUsernameMap.get(session);
////
////        // Direct message to a user using the format "@username <message>"
////        if (message.startsWith("@")) {
////            String destUsername = message.split(" ")[0].substring(1);
////
////            // send the message to the sender and receiver
////            sendMessageToPArticularUser(destUsername, "[DM] " + username + ": " + message);
////            sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);
////
////        }
////        else { // broadcast
////            broadcast(username + ": " + message);
////        }
////
////        // Saving chat history to repository
////        msgRepo.save(new Message(username, message));
////    }
////
////
////    @OnClose
////    public void onClose(Session session) throws IOException {
////        logger.info("Entered into Close");
////
////        // remove the user connection information
////        String username = sessionUsernameMap.get(session);
////        sessionUsernameMap.remove(session);
////        usernameSessionMap.remove(username);
////
////        // broadcase that the user disconnected
////        String message = username + " disconnected";
////        broadcast(message);
////    }
////
////
////    @OnError
////    public void onError(Session session, Throwable throwable) {
////        // Do error handling here
////        logger.info("Entered into Error");
////        throwable.printStackTrace();
////    }
////
////
////    private void sendMessageToPArticularUser(String username, String message) {
////        try {
////            usernameSessionMap.get(username).getBasicRemote().sendText(message);
////        }
////        catch (IOException e) {
////            logger.info("Exception: " + e.getMessage().toString());
////            e.printStackTrace();
////        }
////    }
////
////
////    private void broadcast(String message) {
////        sessionUsernameMap.forEach((session, username) -> {
////            try {
////                session.getBasicRemote().sendText(message);
////            }
////            catch (IOException e) {
////                logger.info("Exception: " + e.getMessage().toString());
////                e.printStackTrace();
////            }
////
////        });
////
////    }
////
////
////    // Gets the Chat history from the repository
////    // cannot autowire static directly (instead we do it by the below
////    // method
////    private static MessageRepository msgRepo;
////
////    /*
////     * Grabs the MessageRepository singleton from the Spring Application
////     * Context.  This works because of the @Controller annotation on this
////     * class and because the variable is declared as static.
////     * There are other ways to set this. However, this approach is
////     * easiest.
////     */
////    @Autowired
////    public void setMessageRepository(MessageRepository repo) {
////        msgRepo = repo;  // we are setting the static variable
////    }
////
////    // Store all socket session and their corresponding username.
////    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
////    private static Map<String, Session> usernameSessionMap = new Hashtable<>();
////
////    private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);
////
////    @OnOpen
////    public void onOpen(Session session, @PathParam("username") String username)
////            throws IOException {
////
////        logger.info("Entered into Open");
////
////        // store connecting user information
////        sessionUsernameMap.put(session, username);
////        usernameSessionMap.put(username, session);
////
////        //Send chat history to the newly connected user
////        sendMessageToPArticularUser(username, getChatHistory());
////
////        // broadcast that new user joined
////        String message = "User:" + username + " has Joined the Chat";
////        broadcast(message);
////    }
////
////
////    private String getChatHistory() {
////        List<Message> messages = msgRepo.findAll();
////
////        // convert the list to a string
////        StringBuilder sb = new StringBuilder();
////        if(messages != null && messages.size() != 0) {
////            for (Message message : messages) {
////                sb.append(message.getUserName() + ": " + message.getContent() + "\n");
////            }
////        }
////        return sb.toString();
////    }
////} // end of Class
