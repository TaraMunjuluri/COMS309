package onetomany.PotentialFriends;

import com.fasterxml.jackson.annotation.JsonIgnore;
import onetomany.Users.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "potential_friends")
public class PotentialFriends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "potential_friend_id")
    private long potentialFriendId;

    private int commonInterestCount;

    @ElementCollection
    @CollectionTable(
            name = "potential_friend_interests",
            joinColumns = @JoinColumn(name = "potential_friend_id")
    )
    @Column(name = "interest")
    private Set<String> commonInterests = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "potential_friend_id", insertable = false, updatable = false)
    @JsonIgnore
    private User potentialFriend;

    // Constructors
    public PotentialFriends() {}

    public PotentialFriends(long userId, long potentialFriendId, Set<String> commonInterests) {
        this.userId = userId;
        this.potentialFriendId = potentialFriendId;
        this.commonInterests = commonInterests;
        this.commonInterestCount = commonInterests.size();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPotentialFriendId() {
        return potentialFriendId;
    }

    public void setPotentialFriendId(long potentialFriendId) {
        this.potentialFriendId = potentialFriendId;
    }

    public int getCommonInterestCount() {
        return commonInterestCount;
    }

    public void setCommonInterestCount(int commonInterestCount) {
        this.commonInterestCount = commonInterestCount;
    }

    public Set<String> getCommonInterests() {
        return commonInterests;
    }

    public void setCommonInterests(Set<String> commonInterests) {
        this.commonInterests = commonInterests;
        this.commonInterestCount = commonInterests.size();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getId();
    }

    public User getPotentialFriend() {
        return potentialFriend;
    }

    public void setPotentialFriend(User potentialFriend) {
        this.potentialFriend = potentialFriend;
        this.potentialFriendId = potentialFriend.getId();
    }
}