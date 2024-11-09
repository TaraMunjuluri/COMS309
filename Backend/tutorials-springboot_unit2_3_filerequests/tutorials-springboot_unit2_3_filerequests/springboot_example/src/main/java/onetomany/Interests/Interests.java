package onetomany.Interests;

import onetomany.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Interests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String major;
    private String classification;

    @ElementCollection
    @CollectionTable(
            name = "interests_interests",  // This matches your table name
            joinColumns = @JoinColumn(name = "interests_id")
    )
    @Column(name = "interests")  // Column name in the collection table
    private List<String> interests;

    @ManyToMany
    @JoinTable(
            name = "user_interests",
            joinColumns = @JoinColumn(name = "interest_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public enum Classification {
        FRESHMAN,
        SOPHOMORE,
        JUNIOR,
        SENIOR
    }

    public Interests() {}

    public Interests(String major, Classification classification, List<String> interests) {
        this.major = major;
        this.classification = String.valueOf(classification);
        this.interests = interests;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
        user.getInterests().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getInterests().remove(this);
    }

    public String getClassification() {
        return classification;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}