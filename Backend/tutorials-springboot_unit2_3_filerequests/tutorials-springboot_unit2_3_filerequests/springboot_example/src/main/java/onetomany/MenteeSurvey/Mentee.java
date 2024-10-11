package onetomany.MenteeSurvey;

import onetomany.Users.User;

import javax.persistence.*;

@Entity
public class Mentee {

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
    private AreaOfMenteeship areaOfMenteeship;

    // Default constructor
    public Mentee() {
    }

    // Constructor with fields (userId, major, classification, areaOfMentorship)
    public Mentee(int userId, String major, Classification classification, AreaOfMenteeship areaOfMenteeship) {
        this.user = user;
        this.major = major;
        this.classification = classification;
        this.areaOfMenteeship = areaOfMenteeship;
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

    public AreaOfMenteeship getAreaOfMenteeship() {
        return areaOfMenteeship;
    }

    public void setAreaOfMenteeship(AreaOfMenteeship areaOfMenteeship) {
        this.areaOfMenteeship = areaOfMenteeship;
    }

    public enum Classification {
        FRESHMAN,
        SOPHOMORE,
        JUNIOR,
        SENIOR,
        GRADUATE_STUDENT
    }

    public enum AreaOfMenteeship {
        CAREER,
        EDUCATION,
        GENERAL
    }
}