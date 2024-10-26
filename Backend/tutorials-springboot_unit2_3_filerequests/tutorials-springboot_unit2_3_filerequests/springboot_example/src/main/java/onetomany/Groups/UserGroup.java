package onetomany.Groups;

import onetomany.Users.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}


//package onetomany.Groups;
//
//import onetomany.Users.User;
//
//import javax.persistence.*;
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//public class Group {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//
//    @ManyToMany(mappedBy = "groups")
//    private Set<User> members = new HashSet<>();
//
//    // Getter for members
//    public Set<User> getMembers() {
//        return members;
//    }
//
//    // Setter for members
//    public void setMembers(Set<User> members) {
//        this.members = members;
//    }
//
//    // Getter and setter for name
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public String toString() {
//        return "Group{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", members=" + members +
//                '}';
//    }
//
//    // Other fields and methods...
//}
//
//
////package onetomany.Groups;
////
////import onetomany.Users.User;
////
////import javax.persistence.*;
////import java.util.ArrayList;
////import java.util.List;
////
////@Entity
////public class Group {
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    private String name;
////
////    @ManyToMany(mappedBy = "groups")
////    private List<User> members = new ArrayList<>();
////
////    // Getter for members
////    public List<User> getMembers() {
////        return members;
////    }
////
////    // Setter for members
////    public void setMembers(List<User> members) {
////        this.members = members;
////    }
////
////    // Getter and setter for name
////    public String getName() {
////        return name;
////    }
////
////    public void setName(String name) {
////        this.name = name;
////    }
////
////    // Other fields and methods...
////}
