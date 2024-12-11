package onetomany.Friends;

import javax.persistence.*;

@Entity
@Table(name = "potential_friends")
public class PotentialFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long potentialFriendId;
    private Integer sharedInterestsCount;

    // Getters and setters
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

    public Long getPotentialFriendId() {
        return potentialFriendId;
    }

    public void setPotentialFriendId(Long potentialFriendId) {
        this.potentialFriendId = potentialFriendId;
    }

    public Integer getSharedInterestsCount() {
        return sharedInterestsCount;
    }

    public void setSharedInterestsCount(Integer sharedInterestsCount) {
        this.sharedInterestsCount = sharedInterestsCount;
    }
}