package onetomany.Friends;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friends")
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "friend_id")
    private Long friendId;

    @Column(name = "friends_since")
    private LocalDateTime friendsSince;

    @Column(name = "shared_interests_count")
    private Integer sharedInterestsCount;

    @Column(name = "user_id")
    private Long userId;

    // Default constructor
    public Friends() {}

    // Constructor with fields
    public Friends(Long userId, Long friendId, Integer sharedInterestsCount) {
        this.userId = userId;
        this.friendId = friendId;
        this.sharedInterestsCount = sharedInterestsCount;
        this.friendsSince = LocalDateTime.now();  // Set current time when creating friendship
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public LocalDateTime getFriendsSince() {
        return friendsSince;
    }

    public void setFriendsSince(LocalDateTime friendsSince) {
        this.friendsSince = friendsSince;
    }

    public Integer getSharedInterestsCount() {
        return sharedInterestsCount;
    }

    public void setSharedInterestsCount(Integer sharedInterestsCount) {
        this.sharedInterestsCount = sharedInterestsCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}