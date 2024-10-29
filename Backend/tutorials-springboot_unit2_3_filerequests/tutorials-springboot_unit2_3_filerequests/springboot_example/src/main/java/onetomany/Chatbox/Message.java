
package onetomany.Chatbox;

import onetomany.Users.User;
import onetomany.Groups.UserGroup;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private UserGroup group;

    private String content;

    private LocalDateTime timestamp;

    // Default constructor
    public Message() {}

    // Constructor to create a message with sender and content
    public Message(User sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now(); // Set the current timestamp
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}





//package onetomany.Chatbox;
//
//import onetomany.Groups.Group;
//import onetomany.Users.User;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "messages")
//public class Message {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User sender;  // User who sent the message
//
//    @ManyToOne
//    @JoinColumn(name = "group_id", nullable = false)
//    private Group group;  // Group to which the message belongs
//
//    @Column(nullable = false)
//    private String content;  // The content of the message
//
//    @Column(nullable = false)
//    private LocalDateTime timestamp;  // Timestamp when the message is sent
//
//    // Default constructor
//    public Message() {
//        this.timestamp = LocalDateTime.now();  // Set timestamp on creation
//    }
//
//
//    // Constructor to initialize sender and content
//    public Message(User sender, String content) {
//        this.sender = sender;
//        this.content = content;
//        this.timestamp = LocalDateTime.now(); // Set timestamp when message is created
//    }
//
//    // Constructor with parameters
//    public Message(User sender, Group group, String content) {
//        this.sender = sender;
//        this.group = group;
//        this.content = content;
//        this.timestamp = LocalDateTime.now();  // Set timestamp on creation
//    }
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public User getSender() {
//        return sender;
//    }
//
//    public void setSender(User sender) {
//        this.sender = sender;
//    }
//
//
//    public Group getGroup() {
//        return group;
//    }
//
//    public void setGroup(Group group) {
//        this.group = group;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public LocalDateTime getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(LocalDateTime timestamp) {
//        this.timestamp = timestamp;
//    }
//}
//
//
//
////package onetomany.Chatbox;
////
////
////import java.util.Date;
////
////import javax.persistence.*;
////
//////import jakarta.persistence.Column;
//////import jakarta.persistence.Entity;
//////import jakarta.persistence.GeneratedValue;
//////import jakarta.persistence.GenerationType;
//////import jakarta.persistence.Id;
//////import jakarta.persistence.Lob;
//////import jakarta.persistence.Table;
//////import jakarta.persistence.Temporal;
//////import jakarta.persistence.TemporalType;
////
////import lombok.Data;
////
////@Entity
////@Table(name = "messages")
////@Data
////public class Message {
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    @Column
////    private String userName;
////
////    @Lob
////    private String content;
////
////    @Temporal(TemporalType.TIMESTAMP)
////    @Column(name = "sent")
////    private Date sent = new Date();
////
////
////    public Message() {};
////
////    public Message(String userName, String content) {
////        this.userName = userName;
////        this.content = content;
////    }
////
////    public Long getId() {
////        return id;
////    }
////
////    public void setId(Long id) {
////        this.id = id;
////    }
////
////    public String getUserName() {
////        return userName;
////    }
////
////    public void setUserName(String userName) {
////        this.userName = userName;
////    }
////
////    public String getContent() {
////        return content;
////    }
////
////    public void setContent(String content) {
////        this.content = content;
////    }
////
////    public Date getSent() {
////        return sent;
////    }
////
////    public void setSent(Date sent) {
////        this.sent = sent;
////    }
////
////
////}
