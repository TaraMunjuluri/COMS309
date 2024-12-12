package onetomany.Friends;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friends")
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long friendId;
    private Integer sharedInterestsCount;
    private LocalDateTime friendsSince;  // Track when they became friends

    // Constructors
    public Friends() {}

    public Friends(Long userId, Long friendId, Integer sharedInterestsCount) {
        this.userId = userId;
        this.friendId = friendId;
        this.sharedInterestsCount = sharedInterestsCount;
        this.friendsSince = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public Integer getSharedInterestsCount() {
        return sharedInterestsCount;
    }

    public void setSharedInterestsCount(Integer sharedInterestsCount) {
        this.sharedInterestsCount = sharedInterestsCount;
    }

    public LocalDateTime getFriendsSince() {
        return friendsSince;
    }

    public void setFriendsSince(LocalDateTime friendsSince) {
        this.friendsSince = friendsSince;
    }
}