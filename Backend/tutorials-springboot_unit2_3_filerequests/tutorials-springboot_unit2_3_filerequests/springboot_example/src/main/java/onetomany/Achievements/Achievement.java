//package onetomany.Achievements;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import onetomany.Users.User;
//
//
//import javax.persistence.*;
//import java.util.HashSet;
//import java.util.Set;
//
//
//@Entity
//@Table(name = "achievements")
//public class Achievement {
//
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//
//    private String name;
//    private String description;
//
//
//    @JsonIgnore
//    @ManyToMany(mappedBy = "achievements")
//    private Set<User> users = new HashSet<>();
//
//
//    // Constructors, getters, and setters
//    public Achievement() {}
//
//
//    public Achievement(String name, String description) {
//        this.name = name;
//        this.description = description;
//    }
//
//
//    public int getId() {
//        return id;
//    }
//
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//
//    public String getName() {
//        return name;
//    }
//
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//
//    public String getDescription() {
//        return description;
//    }
//
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//
//    public Set<User> getUsers() {
//        return users;
//    }
//
//
//    public void setUsers(Set<User> users) {
//        this.users = users;
//    }
//}
