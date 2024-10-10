package onetomany.MentorSurvey;

import onetomany.Users.User;

import javax.persistence.*;

@Entity
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne // Establishes the relationship between Mentor and User
    @JoinColumn(name = "user_id", nullable = false) // Creates the foreign key column for user in the mentor table
    private User user;  // This should match the field you're trying to query in the repository

    //private int userId; // Reference to the User
    private String major;

    @Enumerated(EnumType.STRING)
    private Classification classification;

    @Enumerated(EnumType.STRING)
    private AreaOfMentorship areaOfMentorship;

    // Default constructor
    public Mentor() {
    }

    // Constructor with fields (userId, major, classification, areaOfMentorship)
    public Mentor(int userId, String major, Classification classification, AreaOfMentorship areaOfMentorship) {
        this.user = user;
        this.major = major;
        this.classification = classification;
        this.areaOfMentorship = areaOfMentorship;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public AreaOfMentorship getAreaOfMentorship() {
        return areaOfMentorship;
    }

    public void setAreaOfMentorship(AreaOfMentorship areaOfMentorship) {
        this.areaOfMentorship = areaOfMentorship;
    }

    public enum Classification {
        FRESHMAN,
        SOPHOMORE,
        JUNIOR,
        SENIOR,
        GRADUATE_STUDENT
    }

    public enum AreaOfMentorship {
        CAREER,
        EDUCATION,
        GENERAL
    }
}