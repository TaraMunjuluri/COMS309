package onetomany.PotentialFriends;


import javax.persistence.*;

@Entity
@Table(name = "potential_friends")
public class PotentialFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "potential_friend_id")
    private Long potentialFriendId;

    @Column(name = "shared_interests_count")
    private Integer sharedInterestsCount;

    @Column(name = "common_interest_count")
    private Integer commonInterestCount;

    // Add getters and setters
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

    public Integer getCommonInterestCount() {
        return commonInterestCount;
    }

    public void setCommonInterestCount(Integer commonInterestCount) {
        this.commonInterestCount = commonInterestCount;
    }
}