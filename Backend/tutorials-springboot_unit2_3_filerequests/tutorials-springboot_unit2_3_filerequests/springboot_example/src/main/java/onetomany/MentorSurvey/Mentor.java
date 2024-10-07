package onetomany.MentorSurvey;

import javax.persistence.*;

@Entity
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int userId;
    private String major;

    @Enumerated(EnumType.STRING)
    private Classification classification;

    @Enumerated(EnumType.STRING)
    private AreaOfMentorship areaOfMentorship;

    // Default constructor
    public Mentor() {}

    // Constructor with 4 arguments (userId, major, classification, areaOfMentorship)
    public Mentor(int userId, String major, Classification classification, AreaOfMentorship areaOfMentorship) {
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    // Enum for Classification
    public enum Classification {
        FRESHMAN,
        SOPHOMORE,
        JUNIOR,
        SENIOR,
        GRADUATE_STUDENT
    }

    // Enum for Area of Mentorship
    public enum AreaOfMentorship {
        CAREER,
        EDUCATION,
        GENERAL
    }
}
